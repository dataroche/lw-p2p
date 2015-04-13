package com.p2p;

import java.util.ArrayList;
import java.util.List;

import com.backends.id.SocketId;
import com.nativeMessages.Password;
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
	
	void addPeer(Peer peer){
		info.addPeer();
		connectedPeers.add(peer);
		notifyPeerAdded(peer);
	}
	
	void setNetworkInfo(NetworkInformation info){
		this.info = info;
	}
	
	public NetworkInformation getNetworkInfo(){
		assert info != null;
		return info;
	}
	
	public Iterable<Peer> getAllConnectedPeers(){
		return connectedPeers;
	}
	
	private void notifyPeerAdded(Peer newPeer){
		for(P2PNetworkListener listener : listeners){
			listener.networkPeerConnected(this, newPeer);
		}
	}
	
	private void notifyPeerRemoved(Peer removedPeer){
		for(P2PNetworkListener listener : listeners){
			listener.networkPeerDisconnected(this, removedPeer);
		}
	}

}
