package com.p2p;

import java.net.InetAddress;
import java.net.UnknownHostException;

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

public class NettyServer{
	
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workerGroup;
	private NioEventLoopGroup udpGroup;
	
	private SerializingTable serialTable;
	private P2PNetwork network;
	private boolean connected;
	
	private SocketIdTable idTable;
	private SocketId localId;
	private short nextAssignableId;
	
	private P2PChannelInitializer channelInit;
	
	public NettyServer(P2PNetwork network, int port, SerializingTable serialTable){
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		udpGroup = new NioEventLoopGroup();
		
		connected = false;
		this.serialTable = serialTable;
		this.network = network;

		try {
			this.localId = new SocketId(nextAssignableId++, InetAddress.getLocalHost(), port, port + 1);
			channelInit = new P2PChannelInitializer(localId.getClientId(), serialTable);
			idTable = new SocketIdTable();
			this.nextAssignableId = 0;
			
			initServer(port);
			port++;
			initUdp(port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Unable to initialize local server.");
		}
		
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
		
		bootstrap.bind(port);
		
	}
	
	public void setNextAssignableId(short next){
		nextAssignableId = next;
	}
	
	public short getNextAssignableId(){
		return nextAssignableId;
	}
	
	public void setLocalId(short id){
		idTable.identify(localId.getTcpAddress(), id, localId.getUdpAddress().getPort());
	}
	
	public SocketId getThisId(){
		return localId;
	}
	
	public boolean attemptConnect(SocketId id, Password password){
		boolean canConnect = network.getNetworkInfo().canConnect(password);
		if(!canConnect)
			return false;
		assert (connected); // local id should be defined
			
		idTable.identify(id.getTcpAddress(), nextAssignableId++ , id.getTcpAddress().getPort() + 1);
		Peer peer = new Peer(id);
		peer.join(network);
		return true;
	}
}
