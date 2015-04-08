package com.backends;

import com.backends.id.SocketId;
import com.nativeMessages.ConnectionAttempt;
import com.nativeMessages.Password;
import com.p2p.NettyServer;
import com.p2p.P2PNetwork;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class HandshakeHandler extends
		ChannelInboundHandlerAdapter {
	public static final int HANDSHAKE_TIMEOUT = 10000; //Timeout after 10s if no connection attempt is made.
	
	private NettyServer server;
	
	public HandshakeHandler(NettyServer server){
		this.server = server;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		RawMessage message = (RawMessage) msg;
		if(message.info.getSenderID().isIdentified())//The id is resolved, the peer is already connected.
			ctx.fireChannelRead(message);
		else{//Check for a connection attempt and block all inbound messages
			for(Object o : message.getObjects()){
				if(o instanceof ConnectionAttempt){
					connectionAttempt(ctx, message.info, (ConnectionAttempt) o);
					break;
				}
			}
			if(message.info.getSenderID().getAddedTimeStamp() > System.currentTimeMillis() - HANDSHAKE_TIMEOUT){// Timeout
				ctx.close();
			}
		}
	}
	
	private void connectionAttempt(ChannelHandlerContext ctx, MessageInfo info, ConnectionAttempt attempt){
		SocketId id = info.getSenderID();
		Password passwordAttempt = attempt.getPassword();
		// NEED SOME WAY TO CONNECT A PEER, UPDATE INFO, ADD ID TO CONNECTED ID LIST ? TODO
		boolean connectionSuccess = server.attemptConnect(id, passwordAttempt);
	}
}
