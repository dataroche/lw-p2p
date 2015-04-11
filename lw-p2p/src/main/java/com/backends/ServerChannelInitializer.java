package com.backends;

import com.backends.id.SocketIdTable;
import com.p2p.NettyServer;
import com.p2p.P2PNetwork;
import com.p2p.serializing.UdpSerializingChannelHandler;
import com.p2p.serializing.SerializingTable;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerChannelInitializer extends ChannelInitializer<NioServerSocketChannel>{

	private SocketIdTable idTable;
	private SerializingTable serialTable;
	private NettyServer server;
	
	public ServerChannelInitializer(SocketIdTable idTable, SerializingTable serialTable, NettyServer server){
		this.idTable = idTable;
		this.serialTable = serialTable;
		this.server = server;
	}
	
	@Override
	protected void initChannel(NioServerSocketChannel ch) throws Exception {
		
		
		
		ch.pipeline()
		//[I/O = ???????????????]							// 	Network layer
		.addLast(new TcpPacketHandler(idTable))				// 	Direction:\/|/\, 
		//[/\ = 
		.addLast(new UdpSerializingChannelHandler(serialTable))// 	Direction:\/|/\, Serializes and deserializes objects into the buffer stream.
		//[/\ = MessageRequest] [\/ = RawMessage] 
		.addLast(new HandshakeHandler(server));				//Direction : \/, Intercepts ConnectionAttempt objects and discard others when not connected.
		//[I/O = Objects]
		
	}

}
