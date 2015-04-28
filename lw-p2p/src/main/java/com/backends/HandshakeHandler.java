package com.backends;

import com.backends.id.SocketId;
import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.ConnectionAnswer.Answer;
import com.nativeMessages.ConnectionAttempt;
import com.nativeMessages.NewConnection;
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
			ConnectionAttempt possibleAttempt = message.searchFor(ConnectionAttempt.class);
			if(possibleAttempt != null)//There is a connection attempt
				connectionAttempt(ctx, message.info, possibleAttempt);
					
			if(message.info.getSenderID().getAddedTimeStamp() > System.currentTimeMillis() - HANDSHAKE_TIMEOUT){// Timeout
				ctx.close();
			}
		}
	}
	
	
	
	private void connectionAttempt(ChannelHandlerContext ctx, MessageInfo info, ConnectionAttempt attempt){
		SocketId id = info.getSenderID();
		Password passwordAttempt = attempt.getPassword();
		boolean connectionSuccess = server.attemptConnect(id, passwordAttempt);
		ConnectionAnswer answer;
		if(connectionSuccess){
			answer = new ConnectionAnswer(Answer.SUCCESS);
			answer.setPeerIds(server.getIdTable().getAll());
			answer.setNetworkInformation(server.getNetwork().getNetworkInfo());
			short nextId = server.getNextAssignableId();
			SocketId thisId = server.getThisId();
			int s = thisId.hashCode();
			NewConnection connection = new NewConnection(id, thisId, s, nextId);
			ctx.write(connection);
		}
		else
			answer = new ConnectionAnswer(Answer.FAILURE);
		ctx.writeAndFlush(answer);
	}
}
