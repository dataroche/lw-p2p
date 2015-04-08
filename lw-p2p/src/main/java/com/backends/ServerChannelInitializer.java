package com.backends;

import com.backends.id.SocketIdTable;
import com.p2p.P2PChannelInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerChannelInitializer extends ChannelInitializer<NioServerSocketChannel>{

	private SocketIdTable idTable;
	private P2PChannelInitializer init;
	
	public ServerChannelInitializer(SocketIdTable serialTable, P2PChannelInitializer otherInit){
		this.idTable = serialTable;
		init = otherInit;
	}
	
	@Override
	protected void initChannel(NioServerSocketChannel ch) throws Exception {
		
		
		init.init(ch);
		ch.pipeline().addLast(new TcpPacketHandler(idTable));
	}

}
