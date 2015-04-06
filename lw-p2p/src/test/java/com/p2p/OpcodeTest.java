package com.p2p;

import io.netty.buffer.Unpooled;

import org.junit.Test;

import com.p2p.serializing.NetworkString;
import com.p2p.serializing.NetworkStringSerializer;
import com.p2p.serializing.SerializingTable;

public class OpcodeTest {

	@Test
	public void opcode(){
		String n = "NetworkStringSerializer";
		n.hashCode();
		SerializingTable table = new SerializingTable();
		table.addSerializer(new NetworkStringSerializer());
		table.getSerializer(NetworkStringSerializer.class)
		.encode(Unpooled.buffer(), new NetworkString());
	}
}
