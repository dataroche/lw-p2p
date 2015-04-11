package com.p2p;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;

import com.backends.ChannelMultiplexer;
import com.backends.id.SocketId;

import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;

public class Peer {
	
	/*Descriptive data*/
	private boolean connected;
	private P2PNetwork associatedNetwork;
	private SocketId id;
	
	/*Temporary data until socket initialization*/
	private InetAddress tempAddress;
	private int tempPort;
	
	
	public Peer(SocketId id){
		this.id = id;
		connected = false;
	}
	
	public Peer(InetAddress address, int port){
		tempAddress = address;
		tempPort = port;
		connected = false;
	}
	
	void join(P2PNetwork network){
		associatedNetwork = network;
		network.addPeer(this);
	}
	
	void join(P2PNetwork network, short assignedID, boolean localPeer){
		connected = true;
		associatedNetwork = network;
		id = new SocketId(assignedID, tempAddress, tempPort, tempPort + 1);
	}

	public boolean isConnected() {
		return connected;
	}

	public P2PNetwork getAssociatedNetwork() {
		return associatedNetwork;
	}

	public SocketId getSocketId(){
		return id;
	}
	
}
