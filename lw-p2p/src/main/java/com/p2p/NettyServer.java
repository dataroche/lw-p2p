package com.p2p;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.backends.ServerBossChannelInitializer;
import com.backends.ServerChannelInitializer;
import com.backends.UdpChannelInitializer;
import com.backends.id.SocketId;
import com.backends.id.SocketIdTable;
import com.nativeMessages.NewConnection;
import com.nativeMessages.Password;
import com.p2p.serializing.SerializingTable;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
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
	
	private Channel udpChannel;
	
	
	/**
	 * Used for tests only.
	 * @throws UnknownHostException 
	 */
	public NettyServer(P2PNetwork testNetwork, int port) throws UnknownHostException{
		idTable = new SocketIdTable();
		connected = false;
		this.serialTable = new SerializingTable();
		network = testNetwork;
		
		this.localId = new SocketId(nextAssignableId++, InetAddress.getLocalHost(), port, port + 1);
		
		this.nextAssignableId = 0;
	}
	
	public NettyServer(int port, SerializingTable serialTable) throws UnknownHostException{
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		udpGroup = new NioEventLoopGroup();
		
		idTable = new SocketIdTable();
		connected = false;
		this.serialTable = serialTable;

		
		this.localId = new SocketId(nextAssignableId++, InetAddress.getLocalHost(), port, port + 1);
		
		this.nextAssignableId = 0;
		
		initServer(port);
		port++;
		initUdp(port);
		
		
	}
	
	private void initServer(int port){
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
		.channel(NioServerSocketChannel.class)
		.handler(new ServerBossChannelInitializer())
		.childHandler(new ServerChannelInitializer(this))
		.bind(port);
	}
	
	private void initUdp(int port){
		
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(udpGroup)
		.channel(NioDatagramChannel.class)
		.option(ChannelOption.SO_BROADCAST, true)
		.handler(new UdpChannelInitializer(idTable, serialTable,  this));
		
		try {
			udpChannel = bootstrap.bind(port).sync().channel();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Channel getUdpChannel(){
		return udpChannel;
	}
	
	public SocketIdTable getIdTable(){
		return idTable;
	}
	
	public SerializingTable getSerialTable(){
		return serialTable;
	}
	
	public int getPort(){
		return localId.getTcpAddress().getPort();
	}
	
	void setP2PNetwork(P2PNetwork network){
		this.network = network;
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
	
	public P2PNetwork getNetwork(){
		return network;
	}
	
	public boolean attemptConnect(SocketId id, Password password){
		if(network == null)//We are not connected to any network
			return false;
		
		boolean canConnect = network.getNetworkInfo().canConnect(password);
		if(!canConnect)
			return false;
		assert (connected); // local id should be defined
			
		idTable.identify(id.getTcpAddress(), nextAssignableId++ , id.getTcpAddress().getPort() + 1);
		
		return true;
	}
	
	public boolean connect(NewConnection connection){
		if(connection.getAuthentificatorId().hashCode() != connection.getSignature())
			return false;
		
		Peer peer = new Peer(connection.getNewPeerId());
		peer.join(network);
		
		nextAssignableId = connection.getNextClientId();
		return true;
	}
}
