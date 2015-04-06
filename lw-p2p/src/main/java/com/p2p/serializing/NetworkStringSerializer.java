package com.p2p.serializing;

import io.netty.buffer.ByteBuf;

public class NetworkStringSerializer extends NetworkSerializer<NetworkString> {


	@Override
	protected NetworkString read(ByteBuf stream, int bytesToRead, SerializingTable table) {
		int numberOfChars = bytesToRead/2;//Two bytes per char
		char[] string = new char[numberOfChars];
		for(int i = 0; i < numberOfChars; i ++){
			
			string[i] = stream.readChar();
			
		}
		NetworkString netString = new NetworkString();
		netString.value = String.valueOf(string);
		return netString;
	}
	
	@Override
	protected void write(ByteBuf stream, NetworkString object, SerializingTable table) {
		char[] value = object.value.toCharArray();
		for(char c : value){
			stream.writeChar(c);
		}
	}

	@Override
	protected Class<NetworkString> getSerializableObjectType() {
		return NetworkString.class;
	}

}
