package com.p2p;

import static org.junit.Assert.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.junit.Test;

import com.backends.MessageInfo;
import com.p2p.serializing.NetworkSerializer;
import com.p2p.serializing.NetworkString;
import com.p2p.serializing.SerializingTable;
import com.p2p.serializing.NetworkSerializer.ByteCountCheckException;

public class NetworkSerializerTest {

	ByteBuf buffer = Unpooled.buffer();
	
	@Test(expected= ByteCountCheckException.class)
	public void throwsByteCountException() {
		NetworkSerializer<NetworkString> serial = new NetworkSerializer<NetworkString>() {

			@Override
			protected NetworkString read(ByteBuf stream, int bytesToRead, SerializingTable table) {
				
				return new NetworkString();
			}

			@Override
			protected void write(ByteBuf stream, NetworkString object, SerializingTable table) {
				stream.writeByte(1);
				
			}

			
		};
		serial.encode(buffer, new NetworkString());
		serial.decode(buffer, null);
		
	}

}
