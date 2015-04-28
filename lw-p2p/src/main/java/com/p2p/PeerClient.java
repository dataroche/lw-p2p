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
import java.util.ArrayList;

import com.backends.ChannelMultiplexer;
import com.backends.ClientChannelInitializer;
import com.backends.FlushRequest;
import com.backends.HandshakeListener;
import com.backends.MessagePacker.EmptyMessageRequestException;
import com.backends.id.SocketId;
import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.ConnectionAnswer.Answer;
import com.nativeMessages.ConnectionAttempt;
import com.nativeMessages.NewConnection;

public class PeerClient extends Peer{

	private ChannelMultiplexer mux;
	
	private ArrayList<Peer> connectedPeers;
	
	private NettyServer server;
	
	public PeerClient(NettyServer localServer) throws UnknownHostException {
		super(InetAddress.getLocalHost(), localServer.getPort());
		mux = new ChannelMultiplexer();
		connectedPeers = new ArrayList<Peer>();
		this.server = localServer;
	}
	
	public boolean connectToRemoteNetworkSync(InetSocketAddress address, ConnectionAttempt attempt){
		//TODO connect to remote peer, send connection attempt, 
		//wait for answer, send out new connections and return, all sync.
		Peer firstPeer = connectToRemotePeerSync(address);
		try {
			writeAndFlushTo(attempt, firstPeer).await();
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}
	
	private void waitForAnswer(){
		
	}
	
	private void connectionAnswerReceived(ConnectionAnswer answer){
		if(answer.getAnswer() == Answer.SUCCESS){
			SocketId[] ids = answer.getPeerIds();
		}
	}
	
	public Peer createPeer(InetSocketAddress address){
		SocketId id = server.getIdTable().getID(address);
		return createPeer(id);
	}
	
	public Peer createPeer(SocketId id){
		Peer peer = new Peer(id);
		connectedPeers.add(peer);
		return peer;
	}
	
	public Peer[] createPeers(SocketId ... id){
		Peer[] peers = new Peer[id.length];
		for(int i = 0; i < id.length; i++){
			peers[i] = createPeer(id[i]);
		}
		return peers;
	}
	
	public ChannelFuture connectToRemotePeer(Peer peer){
		EventLoopGroup group = new NioEventLoopGroup();
		return bootstrap(peer.getSocketId().getTcpAddress(), group);
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
	
	public ChannelFuture writeTo(Object o, Peer destination){
		Channel c = getSpecificChannel(destination);
		return c.write(o);
	}
	
	private Channel getSpecificChannel(Peer destination){
		return mux.getChannel(destination);
	}
	
	public void flushTo(Peer destination){
		Channel c = getSpecificChannel(destination);
		c.write(new FlushRequest());
		c.flush();
	}
	
	public ChannelFuture writeAndFlushTo(Object o, Peer destination){
		ChannelFuture future = writeTo(o, destination);
		flushTo(destination);
		return future;
	}
	
}
