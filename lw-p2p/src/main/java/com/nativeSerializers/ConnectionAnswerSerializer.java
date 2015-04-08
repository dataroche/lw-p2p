package com.nativeSerializers;

import io.netty.buffer.ByteBuf;

import com.backends.id.SocketId;
import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.ConnectionAnswer.Answer;
import com.p2p.NetworkInformation;
import com.p2p.serializing.NetworkSerializer;
import com.p2p.serializing.SerializingTable;

public class ConnectionAnswerSerializer extends
		NetworkSerializer<ConnectionAnswer> {

	@Override
	protected void write(ByteBuf stream, ConnectionAnswer object,
			SerializingTable table) {
		stream.writeByte(object.getAnswer().ordinal()); // Answer ordinal
		SocketId[] peerIds = object.getPeerIds();
		table.write(stream, object.getNetworkInformation());// Network info
		stream.writeByte(peerIds.length); // Write socket ids byte count
		for(SocketId id : peerIds)
			table.write(stream, id); //Write each socket id to the stream

	}

	@Override
	protected ConnectionAnswer read(ByteBuf stream, int bytesToRead,
			SerializingTable table) {
		int enumOrdinal = stream.readByte(); // Answer ordinal
		Answer answer = Answer.values()[enumOrdinal];
		NetworkInformation info = (NetworkInformation) table.readSimple(stream); // Network info
		int peerCount = stream.readByte(); // socket ids count
		SocketId[] socketIds = new SocketId[peerCount];
		for(int i = 0; i < peerCount; i++)
			socketIds[i] = (SocketId) table.readSimple(stream); // socket ids
		
		ConnectionAnswer conAns = new ConnectionAnswer(answer);
		conAns.setPeerIds(socketIds);
		conAns.setNetworkInformation(info);
		return conAns;
	}

	@Override
	protected Class<ConnectionAnswer> getSerializableObjectType() {
		return ConnectionAnswer.class;
	}

}
