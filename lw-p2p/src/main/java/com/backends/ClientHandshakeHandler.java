package com.backends;

import com.backends.id.SocketId;
import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.ConnectionAnswer.Answer;
import com.nativeMessages.NewConnection;
import com.p2p.NetworkInformation;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handler overview : <br>
 * Reads in {@link RawMessage}.<br>
 * Searches for {@link ConnectionAnswer}<br>
 * if found -><br>
 * Checks for connection success<br>
 * if successful -><br>
 * Writes a ConnectionBroadcastRequest<br>
 * <p>
 * Searches for {@link NetworkWelcome}<br>
 * if found -><br>
 * Increments a counter until all peers have accepted connection, and notifies of the success.<br>
 * <p>
 * Notifies of a connection failure under either of these circumstances :<br>
 * 1- The connection answer is negative, or<br>
 * 2- One or more peers have failed to answer to the connection request after a certain timeout.
 * 
 * 
 * 
 * @author WILL
 *
 */
public class ClientHandshakeHandler extends ChannelInboundHandlerAdapter {
	
	public static enum HandshakeStatus{pending, acknowledgePending, completed};
	
	private HandshakeListener soleListener;
	private NetworkInformation receivedInformation;
	private HandshakeStatus status;
	
	public ClientHandshakeHandler(HandshakeListener listener){
		soleListener = listener;
		status = HandshakeStatus.pending;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		RawMessage message = (RawMessage) msg;
		switch(status){
		case pending : 
			checkForConnectionAnswer(ctx, message);
			break;	
		case completed :
			break;
		}
	}
	
	private void checkForConnectionAnswer(ChannelHandlerContext ctx, RawMessage msg){
		ConnectionAnswer answer = msg.searchFor(ConnectionAnswer.class);
		if(answer == null)
			return;
		Answer connectionSuccess = answer.getAnswer();
		if(connectionSuccess != Answer.SUCCESS)
			soleListener.connectionFailed();
		else{
			NewConnection connection = msg.searchFor(NewConnection.class);
			soleListener.connectionAccepted(answer.getNetworkInformation());
			receivedInformation = answer.getNetworkInformation();
		}
	}
	
	@Override
	public boolean isSharable() {
		return false;
	}
	
	/**Network welcome counter, to make sure everyone on the network accepted the connection
	 * 
	 * @author WILL
	 *
	 */
	private class ConnectionCounter{
		int count = 0;
		int objective;
		
		private ConnectionCounter(){
			
		}
		
		private boolean increment(){
			count++;
		}
		
	}
}
