package com.backends;

import com.backends.id.SocketIdTable;
import com.p2p.NettyServer;
import com.p2p.serializing.SerializingChannelHandler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private NettyServer server;

	public ClientChannelInitializer(NettyServer server){
		this.server = server;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline()
		//[I/O = ByteBuf]									// 	Network layer
		.addLast(new TcpPacketHandler(server.getIdTable()))				// 	Direction:\/|/\, 
		//[/\ = 
		.addLast(new SerializingChannelHandler(server.getSerialTable()))// 	Direction:\/|/\, Serializes and deserializes objects into the buffer stream.
		//[/\ = MessageRequest] [\/ = RawMessage] 
		.addLast(new HandshakeHandler(server));				//Direction : \/, Intercepts ConnectionAttempt objects and discard others when not connected.
		//[I/O = Objects]
		
	}

}
