package com.backends;

import com.p2p.NettyServer;
import com.p2p.serializing.SerializingChannelHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TcpTestingPipeline extends ChannelInitializer<NioServerSocketChannel>{

	private NettyServer server;
	
	public TcpTestingPipeline(NettyServer server){
		this.server = server;
	}
	
	@Override
	protected void initChannel(NioServerSocketChannel ch) throws Exception {
		ch.pipeline()
		//[I/O = ByteBuf]									// 	Network layer
		.addLast(new TcpPacketHandler(server.getIdTable()))				// 	Direction:\/|/\, 
		//[/\ = 
		.addLast(new SerializingChannelHandler(server.getSerialTable()));// 	Direction:\/|/\, Serializes and deserializes objects into the buffer stream.
		
	}

}
