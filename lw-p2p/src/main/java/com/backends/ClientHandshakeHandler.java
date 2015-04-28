package com.backends;

import com.backends.id.SocketId;
import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.ConnectionAnswer.Answer;
import com.nativeMessages.NewConnection;
import com.p2p.NetworkInformation;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

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
			soleListener.connectionToNetworkFailed();
		else{
			NewConnection connection = msg.searchFor(NewConnection.class);
			soleListener.peersRequestingNewConnections(answer, connection);
			receivedInformation = answer.getNetworkInformation();
		}
	}
	
	@Override
	public boolean isSharable() {
		return false;
	}
}
