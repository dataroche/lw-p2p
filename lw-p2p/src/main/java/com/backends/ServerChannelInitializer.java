package com.backends;

import com.backends.id.SocketIdTable;
import com.p2p.NettyServer;
import com.p2p.P2PNetwork;
import com.p2p.serializing.SerializingChannelHandler;
import com.p2p.serializing.SerializingTable;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerChannelInitializer extends ChannelInitializer<NioServerSocketChannel>{

	private SocketIdTable idTable;
	private SerializingTable serialTable;
	private NettyServer server;
	
	public ServerChannelInitializer(NettyServer server){
		this.idTable = server.getIdTable();
		this.serialTable = server.getSerialTable();
		this.server = server;
	}
	
	@Override
	protected void initChannel(NioServerSocketChannel ch) throws Exception {
		
		ch.pipeline()
		//[I/O = ByteBuf]									// 	Network layer
		.addLast(new TcpPacketHandler(idTable))				// 	Direction:\/|/\, 
		//[/\ = 
		.addLast(new SerializingChannelHandler(serialTable))// 	Direction:\/|/\, Serializes and deserializes objects into the buffer stream.
		//[/\ = MessageRequest] [\/ = RawMessage] 
		.addLast(new MessagePacker())
		//[I/O = Objects]
		.addLast(new HandshakeHandler(server));				//Direction : \/, Intercepts ConnectionAttempt objects and discard others when not connected.
		//[I/O = Objects]
		
	}

}
