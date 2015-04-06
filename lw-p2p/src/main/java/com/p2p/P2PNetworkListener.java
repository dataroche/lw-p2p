package com.p2p;


public interface P2PNetworkListener {
	
	public void networkPeerConnected(P2PNetwork network, Peer peer);
	public void networkPeerDisconnected(P2PNetwork network, Peer peer, DisconnectReason reason);
	public void networkPasswordProtectedStatusChanged(P2PNetwork network);
	
}
