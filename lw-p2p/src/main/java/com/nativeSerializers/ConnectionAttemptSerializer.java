package com.nativeSerializers;

import io.netty.buffer.ByteBuf;

import com.nativeMessages.ConnectionAttempt;
import com.nativeMessages.Password;
import com.p2p.serializing.NetworkSerializer;
import com.p2p.serializing.SerializingTable;

public class ConnectionAttemptSerializer extends
		NetworkSerializer<ConnectionAttempt> {

	@Override
	protected void write(ByteBuf stream, ConnectionAttempt object,
			SerializingTable table) {
		stream.writeBoolean(object.isRequestingPeerAddresses());
		table.write(stream, object.getPassword());
	}

	@Override
	protected ConnectionAttempt read(ByteBuf stream, int bytesToRead,
			SerializingTable table) {
		boolean requestingIps = stream.readBoolean();
		Password password = (Password) table.readNext(stream, null);
		ConnectionAttempt attempt = new ConnectionAttempt(password);
		if(!requestingIps)
			attempt.doNotRequestPeerAddresses();
		return attempt;
	}

	@Override
	protected Class<ConnectionAttempt> getSerializableObjectType() {
		return ConnectionAttempt.class;
	}

}
