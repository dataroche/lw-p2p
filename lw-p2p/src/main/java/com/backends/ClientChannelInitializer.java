package com.backends;

import com.backends.id.SocketIdTable;
import com.p2p.NettyServer;
import com.p2p.serializing.UdpSerializingChannelHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	public ClientChannelInitializer(NettyServer server){
		
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline()
		//[I/O = ByteBuf]									// 	Network layer
		.addLast(new TcpPacketHandler(null))				// 	Direction:\/|/\, 
		//[/\ = 
		.addLast(new UdpSerializingChannelHandler(serialTable))// 	Direction:\/|/\, Serializes and deserializes objects into the buffer stream.
		//[/\ = MessageRequest] [\/ = RawMessage] 
		.addLast(new HandshakeHandler(server));				//Direction : \/, Intercepts ConnectionAttempt objects and discard others when not connected.
		//[I/O = Objects]
		
	}

}
