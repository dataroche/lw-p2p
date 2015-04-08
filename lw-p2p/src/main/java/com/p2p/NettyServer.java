package com.p2p;

import com.backends.ServerBossChannelInitializer;
import com.backends.ServerChannelInitializer;
import com.backends.UdpChannelInitializer;
import com.backends.id.SocketId;
import com.backends.id.SocketIdTable;
import com.nativeMessages.Password;
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
	private P2PNetwork network;
	
	private SocketIdTable idTable;
	private short localId;
	private short nextAssignableId;
	
	private P2PChannelInitializer channelInit;
	
	public NettyServer(P2PNetwork network, int port, SerializingTable serialTable){
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		udpGroup = new NioEventLoopGroup();
		
		this.serialTable = serialTable;
		this.network = network;
		channelInit = new P2PChannelInitializer(localId, serialTable);
		idTable = new SocketIdTable();
		this.localId = -1;
		this.nextAssignableId = -1;
		
		initServer(port);
		port++;
		initUdp(port);
	}
	
	private void initServer(int port){
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
		.channel(NioServerSocketChannel.class)
		.handler(new ServerBossChannelInitializer())
		.childHandler(new ServerChannelInitializer(idTable, this, channelInit))
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
	
	public boolean attemptConnect(SocketId id, Password password){
		boolean canConnect = network.getNetworkInfo().canConnect(password);
		if(!canConnect)
			return false;
		assert (localId < 0); // local id should be defined
			
		idTable.identify(id.getTcpAddress(), nextAssignableId++ , id.getTcpAddress().getPort() + 1);
		Peer peer = new Peer(id);
		peer.connect(network);
		return true;
	}

	public void operationComplete(ChannelFuture future) throws Exception {
		
		
	}
}
