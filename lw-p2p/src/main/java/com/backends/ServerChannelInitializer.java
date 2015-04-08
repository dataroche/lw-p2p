package com.backends;

import com.backends.id.SocketIdTable;
import com.p2p.NettyServer;
import com.p2p.P2PChannelInitializer;
import com.p2p.P2PNetwork;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerChannelInitializer extends ChannelInitializer<NioServerSocketChannel>{

	private SocketIdTable idTable;
	private NettyServer server;
	private P2PChannelInitializer init;
	
	public ServerChannelInitializer(SocketIdTable serialTable, NettyServer server,  P2PChannelInitializer otherInit){
		this.idTable = serialTable;
		this.server = server;
		init = otherInit;
	}
	
	@Override
	protected void initChannel(NioServerSocketChannel ch) throws Exception {
		
		
		
		ch.pipeline().addFirst(new HandshakeHandler(server));
		init.init(ch);
		ch.pipeline().addLast(new TcpPacketHandler(idTable));
	}

}
