package com.backends;

import com.backends.id.SocketIdTable;
import com.p2p.P2PChannelInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpChannelInitializer extends ChannelInitializer<NioDatagramChannel>{
	
	private SocketIdTable idTable;
	private P2PChannelInitializer init;
	
	public UdpChannelInitializer(SocketIdTable idTable, P2PChannelInitializer otherInit){
		this.idTable = idTable;
		init = otherInit;
	}
	
	@Override
	protected void initChannel(NioDatagramChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		init.init(ch);
		pipeline.addLast(new UdpPacketHandler(idTable));
	}
	
	

}
