package com.nativeSerializers;

import com.nativeMessages.NetworkWelcome;
import com.p2p.serializing.EmptySerializer;

public class NetworkWelcomeSerializer extends EmptySerializer<NetworkWelcome> {

	@Override
	protected Class<NetworkWelcome> getSerializableObjectType() {
		return NetworkWelcome.class;
	}

}
