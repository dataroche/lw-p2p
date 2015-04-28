package com.backends;

import com.backends.id.SocketId;
import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.NewConnection;
import com.p2p.NetworkInformation;

public interface HandshakeListener {
	public void peersRequestingNewConnections(ConnectionAnswer answer, NewConnection thisConnection);
	public void connectionToNetworkSuccessful();
	public void connectionToNetworkFailed();
}
