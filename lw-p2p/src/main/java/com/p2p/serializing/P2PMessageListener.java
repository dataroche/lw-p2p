package com.p2p.serializing;

import com.backends.MessageInfo;

public interface P2PMessageListener<Type> {
	public void messageReceived(Type message, MessageInfo info);
}
