package com.backends;

import com.backends.id.SocketIdTable;
import com.p2p.P2PChannelInitializer;
import com.p2p.serializing.SerializingTable;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioDatagramChannel;
import io.netty.handler.logging.LoggingHandler;

public class NettyServer implements ChannelFutureListener{
	
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workerGroup;
	private NioEventLoopGroup udpGroup;
	
	private SerializingTable serialTable;
	
	private SocketIdTable idTable;
	private short localId;
	
	private P2PChannelInitializer channelInit;
	
	public NettyServer(int port, short localId, SerializingTable serialTable){
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		udpGroup = new NioEventLoopGroup();
		
		this.serialTable = serialTable;
		channelInit = new P2PChannelInitializer(localId, serialTable);
		idTable = new SocketIdTable();
		this.localId = localId;
		
		initServer(port);
		port++;
		initUdp(port);
	}
	
	private void initServer(int port){
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
		.channel(NioServerSocketChannel.class)
		.handler(new ServerBossChannelInitializer())
		.childHandler(new ServerChannelInitializer(idTable, channelInit))
		.bind(port);
	}
	
	private void initUdp(int port){
		
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(udpGroup)
		.channel(NioDatagramChannel.class)
		.option(ChannelOption.SO_BROADCAST, true)
		.handler(new UdpChannelInitializer(idTable, channelInit));
		
		bootstrap.bind(port).addListener(this);
		
	}

	public void operationComplete(ChannelFuture future) throws Exception {
		
		
	}
}
