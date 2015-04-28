package com.backends;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.group.ChannelGroup;

import org.junit.Test;

import com.backends.id.SocketIdTable;
import com.p2p.Peer;

public class ChannelMultiplexerTest {

	private ChannelMultiplexer mux = new ChannelMultiplexer();
	private Peer[] p = new Peer[]{new Peer(InetSocketAddress.createUnresolved("a", 555)),
			new Peer(InetSocketAddress.createUnresolved("b", 555)),
			new Peer(InetSocketAddress.createUnresolved("c", 555)),
			new Peer(InetSocketAddress.createUnresolved("d", 555)),
			new Peer(InetSocketAddress.createUnresolved("e", 555)),
			new Peer(InetSocketAddress.createUnresolved("f", 555)),
			new Peer(InetSocketAddress.createUnresolved("g", 555)),
			new Peer(InetSocketAddress.createUnresolved("h", 555)),
			new Peer(InetSocketAddress.createUnresolved("i", 555)),
			new Peer(InetSocketAddress.createUnresolved("j", 555)),
			new Peer(InetSocketAddress.createUnresolved("k", 555)),
			new Peer(InetSocketAddress.createUnresolved("l", 555))};
	
	private SocketIdTable idTable = new SocketIdTable();
	private EmbeddedChannel c[] =  {new EmbeddedChannel(new TcpPacketHandler(idTable)),
			new EmbeddedChannel(new TcpPacketHandler(idTable)),
			new EmbeddedChannel(new TcpPacketHandler(idTable)),
			new EmbeddedChannel(new TcpPacketHandler(idTable)),
			new EmbeddedChannel(new TcpPacketHandler(idTable)),
			new EmbeddedChannel(new TcpPacketHandler(idTable)),
			new EmbeddedChannel(new TcpPacketHandler(idTable)),
			new EmbeddedChannel(new TcpPacketHandler(idTable)),
			new EmbeddedChannel(new TcpPacketHandler(idTable)),
			new EmbeddedChannel(new TcpPacketHandler(idTable)),
			new EmbeddedChannel(new TcpPacketHandler(idTable)),
			new EmbeddedChannel(new TcpPacketHandler(idTable))};
	
	@Test
	public void addSingleChannel() {
		mux.addChannel(p[0], c[0]);
		assertTrue(mux.getChannel(p[0]) == c[0]);
	}
	
	@Test
	public void addMultipleChannels(){
		for(int i = 0; i < p.length; i++){
			mux.addChannel(p[i], c[i]);
		}
		
		ChannelGroup group = mux.getAllChannels();
		for(int i = 0; i < p.length; i++){
			assertTrue(mux.getChannel(p[i]) == c[i]);
			assertTrue(group.contains(c[i]));
		}
		
	}

}
