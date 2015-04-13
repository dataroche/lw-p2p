package com.p2p;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.telnet.TelnetClientInitializer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.backends.ChannelMultiplexer;
import com.backends.ClientChannelInitializer;
import com.backends.FlushRequest;
import com.backends.MessagePacker.EmptyMessageRequestException;
import com.backends.id.SocketId;
import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.ConnectionAnswer.Answer;

public class PeerClient extends Peer {

	private ChannelMultiplexer mux;
	
	private NettyServer server;
	
	public PeerClient(NettyServer localServer) throws UnknownHostException {
		super(InetAddress.getLocalHost(), localServer.getPort());
		this.server = localServer;
	}
	
	public Peer connectToRemotePeerSync(InetSocketAddress address){
		SocketId id = server.getIdTable().getID(address);
		EventLoopGroup group = new NioEventLoopGroup();
		Peer peer = new Peer(id);
		try {
			Channel c = bootstrap(address, group).sync().channel();
			mux.addChannel(peer, c);
		} catch (InterruptedException e) {
			return null;
		} 
		return peer;
	}
	
	public boolean connectToRemoteNetworkSync(InetSocketAddress address){
		//TODO connect to remote peer, send connection attempt, wait for answer, send out new connections and return, all sync.
	}
	
	public void connectionAnswerReceived(ConnectionAnswer answer){
		if(answer.getAnswer() == Answer.SUCCESS){
			SocketId[] ids = answer.getPeerIds();
		}
	}
	
	private ChannelFuture bootstrap(InetSocketAddress address, EventLoopGroup group){
		Bootstrap b = new Bootstrap();
		
		b.group(group)
        .channel(NioSocketChannel.class)
        .handler(new ClientChannelInitializer(server));
		
		return b.connect(address);
	}
	
	public void write(Object o){
		mux.getAllChannels().write(o);
	}
	
	public void flush(){
		mux.getAllChannels().write(new FlushRequest());
	}
	
	public void writeAndFlush(Object o){
		write(o);
		flush();
	}
	
	public void writeTo(Object o, Peer destination){
		Channel c = getSpecificChannel(destination);
		c.write(o);
	}
	
	private Channel getSpecificChannel(Peer destination){
		return mux.getChannel(destination);
	}
	
	public void flushTo(Peer destination){
		Channel c = getSpecificChannel(destination);
		c.write(new FlushRequest());
		c.flush();
	}
	
	public void writeAndFlushTo(Object o, Peer destination){
		writeTo(o, destination);
		flushTo(destination);
	}
	
}
