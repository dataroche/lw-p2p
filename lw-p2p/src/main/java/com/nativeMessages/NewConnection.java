package com.nativeMessages;

import com.backends.id.SocketId;

public class NewConnection {
	private SocketId newPeerId;
	private SocketId authentificatorId;
	private int signature;
	private short nextClientId;
	
	public NewConnection(SocketId newPeer, SocketId auth, int signature, short nextAssignableId){
		this.newPeerId = newPeer;
		this.authentificatorId = auth;
		this.signature = signature;
		this.nextClientId = nextAssignableId;
	}

	public SocketId getNewPeerId() {
		return newPeerId;
	}

	public SocketId getAuthentificatorId() {
		return authentificatorId;
	}

	public int getSignature() {
		return signature;
	}

	public short getNextClientId() {
		return nextClientId;
	}
}
