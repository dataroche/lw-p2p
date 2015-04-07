package com.nativeSerializers;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import io.netty.buffer.ByteBuf;

import com.backends.id.SocketId;
import com.p2p.serializing.NetworkSerializer;
import com.p2p.serializing.SerializingTable;

public class SocketIdSerializer extends NetworkSerializer<SocketId> {

	@Override
	protected void write(ByteBuf stream, SocketId object, SerializingTable table) {
		InetSocketAddress address = object.getTcpAddress();
		byte[] rawAddress = address.getAddress().getAddress();
		
		stream.writeByte(rawAddress.length); //Address byte count
		for(byte b : rawAddress)
			stream.writeByte(b); //Address bytes
		
		stream.writeInt(address.getPort()); //Tcp port
		stream.writeInt(object.getUdpAddress().getPort()); //Udp port
		stream.writeShort(object.getClientId()); //Client id
		
	}

	@Override
	protected SocketId read(ByteBuf stream, int bytesToRead,
			SerializingTable table) {
		byte[] rawAddress = new byte[stream.readByte()]; //Read the address byte count
		for(int i = 0; i < rawAddress.length; i++)
			rawAddress[i] = stream.readByte();
		
		int tcpPort = stream.readInt();
		int udpPort = stream.readInt();
		short clientId = stream.readShort();
		SocketId id = null;
		try {
			id = new SocketId(clientId, InetAddress.getByAddress(rawAddress), tcpPort, udpPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return id;
	}

	@Override
	protected Class<SocketId> getSerializableObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

}
