package com.backends;

import com.backends.id.SocketId;
import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.NewConnection;
import com.p2p.NetworkInformation;

public interface HandshakeListener {
	public void connectionAccepted(NetworkInformation info);
	public void connectionSuccessful();
	public void connectionFailed();
}
