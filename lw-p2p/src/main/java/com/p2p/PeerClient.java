package com.p2p;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.backends.ChannelMultiplexer;
import com.backends.FlushRequest;
import com.backends.MessagePacker.EmptyMessageRequestException;

public class PeerClient extends Peer {

	private ChannelMultiplexer mux;
	private Channel udpChannel;
	
	public PeerClient(int port) throws UnknownHostException {
		super(InetAddress.getLocalHost(), port);
	}
	
	public boolean connectToRemotePeerSync(InetSocketAddress address){
		mux.addChannel(peer.getSocketId().getClientId(), channel);
	}
	
	private boolean bootstrap(InetSocketAddress address){
		Bootstrap b = new Bootstrap();
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
	
	private Channel getSpecificChannel(Peer destination){
		return mux.getChannel(destination.getSocketId().getClientId());
	}
	
	public void writeTo(Object o, Peer destination){
		Channel c = getSpecificChannel(destination);
		c.write(o);
	}
	
	public void flushTo(Peer destination){
		Channel c = getSpecificChannel(destination);
		c.flush();
	}
	
	public void writeAndFlushTo(Object o, Peer destination){
		writeTo(o, destination);
		flushTo(destination);
	}
	
}
