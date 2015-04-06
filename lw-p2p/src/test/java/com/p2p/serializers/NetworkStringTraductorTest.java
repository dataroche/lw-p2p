package com.p2p.serializers;

import static org.junit.Assert.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.junit.Test;

import com.backends.MessageInfo;
import com.p2p.serializing.NetworkSerializer;
import com.p2p.serializing.NetworkString;
import com.p2p.serializing.NetworkStringSerializer;
import com.p2p.serializing.NetworkSerializer.ByteCountCheckException;

public class NetworkStringTraductorTest {
	
	@Test
	public void objectEqualsBeforeWriteAndAfterRead() throws ByteCountCheckException {
		String value = "abcdefg";
		NetworkString string = new NetworkString();
		string.value = value;
		ByteBuf buffer = Unpooled.buffer();
		
		NetworkStringSerializer traductor = new NetworkStringSerializer();
		
		traductor.encode(buffer, string);
		NetworkString readString = traductor.decode(buffer, null);
		
		assertEquals(readString.value, value);
		
	}
	
	@Test
	public void opcodeValid(){
		NetworkSerializer<NetworkString> otherSerializer = new NetworkSerializer<NetworkString>() {
			
			@Override
			protected void write(ByteBuf stream, NetworkString object) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected NetworkString read(ByteBuf stream, int bytesToRead) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		NetworkStringSerializer serializer = new NetworkStringSerializer();
		
		assertNotEquals(serializer.getOpcode(), otherSerializer.getOpcode());
	}

}
