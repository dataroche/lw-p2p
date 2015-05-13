package com.backends;

import com.backends.id.SocketIdTable;
import com.p2p.NettyServer;
import com.p2p.NetworkAccess;
import com.p2p.serializing.SerializingChannelHandler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private NetworkAccess access;

	public ClientChannelInitializer(NetworkAccess access){
		this.access = access;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline()
		//[I/O = ByteBuf]									// 	Network layer
		.addLast(new TcpPacketHandler(access.getServer().getIdTable()))				// 	Direction:\/|/\, 
		//[/\ = 
		.addLast(new SerializingChannelHandler(access.getServer().getSerialTable()))// 	Direction:\/|/\, Serializes and deserializes objects into the buffer stream.
		//[/\ = MessageRequest] [\/ = RawMessage] 
		.addLast(new MessagePacker())
		.addLast(new ClientHandshakeHandler(access));				//Direction : \/, Intercepts ConnectionAttempt objects and discard others when not connected.
		//[I/O = Objects]
		
	}

}
