package com.p2p;

import io.netty.buffer.ByteBuf;

import com.nativeMessages.Password;
import com.p2p.serializing.NetworkSerializer;
import com.p2p.serializing.SerializingTable;

public class NetworkInformationSerializer extends
		NetworkSerializer<NetworkInformation> {

	@Override
	protected void write(ByteBuf stream, NetworkInformation object,
			SerializingTable table) throws RuntimeException {
		table.write(stream, object.getName());
		stream.writeShort(object.getMaxConnectedPeers());
		stream.writeShort(object.getConnectedPeers());
		stream.writeBoolean(object.isAcceptingConnections());
		boolean passwordProtected = object.isPasswordProtected();
		stream.writeBoolean(passwordProtected);
		if(passwordProtected)
			table.write(stream, object.getPassword());
		
	}

	@Override
	protected NetworkInformation read(ByteBuf stream, int bytesToRead,
			SerializingTable table) throws RuntimeException {
		NetworkInformation info = null;
		String name = (String) table.readSimple(stream);
		short maxConnectedPeers = stream.readShort();
		short connectedPeers = stream.readShort();
		boolean acceptingConnections = stream.readBoolean();
		boolean passwordProtected = stream.readBoolean();
		info = new NetworkInformation(name, maxConnectedPeers);
		if(passwordProtected){
			Password password = (Password) table.readSimple(stream);
			info.setPassword(password);
		}
		info.addPeer(connectedPeers);
		info.setAcceptingConnections(acceptingConnections);
		return info;
	}

	@Override
	protected Class<NetworkInformation> getSerializableObjectType() {
		return NetworkInformation.class;
	}

}
