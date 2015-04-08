package com.p2p;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;

import com.backends.id.SocketId;

import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;

public class Peer {
	private boolean connected;
	private P2PNetwork associatedNetwork;
	private InetAddress tempAddress;
	private int tempPort;
	private SocketId id;
	private boolean local;
	
	public Peer(SocketId id){
		this.id = id;
		connected = false;
		local = false;
	}
	
	public Peer(InetAddress address, int port){
		tempAddress = address;
		tempPort = port;
		connected = false;
		local = false;
	}
	
	void connect(P2PNetwork network){
		associatedNetwork = network;
		network.addPeer(this);
	}
	
	void connect(P2PNetwork network, short assignedID, boolean localPeer){
		connected = true;
		local = localPeer;
		associatedNetwork = network;
		id = new SocketId(assignedID, tempAddress, tempPort, tempPort + 1);
	}

	public boolean isLocal() {
		return local;
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
