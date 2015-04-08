package com.p2p;

import java.util.ArrayList;
import java.util.List;

import com.p2p.serializing.SerializingTable;


public class P2PNetwork {
	
	
	/**
	 * All connected peers. The first in the list is the host.
	 */
	private List<Peer> connectedPeers;
	
	private List<P2PNetworkListener> listeners;
	
	private NetworkInformation info;
	
	P2PNetwork(){
		connectedPeers = new ArrayList<Peer>();
		listeners = new ArrayList<P2PNetworkListener>();
	}
	
	public void broadcast(Object... objects){
		for(Object o : objects)
			broadcast(o);
	}
	
	public void broadcast(Object object){
		
	}
	
	public void sendTo(Peer peer, Object ... objects){
		for(Object o : objects)
			sendTo(peer, o);
	}
	
	public void sendTo(Peer peer, Object object){
		
	}
	
	public void sendToHost(Object ... objects){
		for(Object o : objects)
			sendToHost(o);
	}
	
	public void sendToHost(Object object){
		
	}
	
	public boolean connect()
	
	public NetworkInformation getNetworkInfo(){
		assert info != null;
		return info;
	}
	
	public Iterable<Peer> getAllConnectedPeers(){
		return connectedPeers;
	}

}
