package com.p2p;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.junit.Test;

import com.backends.MessageInfo;
import com.p2p.serializing.NetworkSerializer;
import com.p2p.serializing.NetworkString;
import com.p2p.serializing.NetworkStringSerializer;
import com.p2p.serializing.P2PMessageListener;
import com.p2p.serializing.SerializingTable;

public class SerializingTableTest {
	SerializingTable table = new SerializingTable();
	ByteBuf buffer = Unpooled.buffer();
	NetworkStringSerializer serial = new NetworkStringSerializer();
	
	@Test
	public void writeObject(){
		NetworkString string= new NetworkString();
		string.value = "patate";
		table.addSerializer(serial);
		table.write(buffer, string);
	}
	
	@Test
	public void readObject(){
		NetworkString string= new NetworkString();
		string.value = "patate";
		table.addSerializer(serial);
		table.write(buffer, string);
		
		NetworkString readString = (NetworkString) table.readNext(buffer, null);
		assertEquals(string.value, readString.value);
	}
	
	@Test(expected = SerializingTable.NoSerializerForSerializableException.class)
	public void noSerializer() {
		table.write(buffer, new NetworkString());
		
	}
	
	@Test(expected = SerializingTable.OpcodeUnknownException.class)
	public void unknownOpcode() {
		buffer.writeShort(6);
		table.readNext(buffer, null);
	}
	
	private static class RecursiveTestSerializable{

		ArrayList<NetworkString> strings;
		
		public Class<? extends NetworkSerializer<?>> getSerializerClass() {
			
			return RecursiveTestSerializer.class;
		}
		
	}
	
	private static class RecursiveTestSerializer extends NetworkSerializer<RecursiveTestSerializable>{

		@Override
		protected void write(ByteBuf stream, RecursiveTestSerializable object,
				SerializingTable table) {
			ArrayList<NetworkString> strings = object.strings;
			stream.writeShort(strings.size());
			
			for(NetworkString string : strings)
				table.write(stream, string);
			
		}

		@Override
		protected RecursiveTestSerializable read(ByteBuf stream,
				int bytesToRead, SerializingTable table) {
			int stringsToRead = stream.readShort();
			RecursiveTestSerializable test = new RecursiveTestSerializable();
			test.strings = new ArrayList<NetworkString>();
			for(int i = 0; i < stringsToRead; i++)
				test.strings.add((NetworkString) table.readNext(stream, null));
			
			return test;
		}

		@Override
		protected Class<RecursiveTestSerializable> getSerializableObjectType() {
			return RecursiveTestSerializable.class;
		}
		
	}
	
	private RecursiveTestSerializable buildFrom(String ... s){
		RecursiveTestSerializable test = new RecursiveTestSerializable();
		test.strings = new ArrayList<NetworkString>();
		for(int i = 0; i < s.length; i++){
			NetworkString string = new NetworkString();
			string.value = s[i];
			test.strings.add(string);
		}
		return test;
	}
	
	@Test
	public void recursiveSerializable(){
		
		//Build an object containing three strings : aaaaa, bbbbb, ccccc.
		String[] s = new String[]{"aaaaa", "bbbbb", "ccccc"};
		RecursiveTestSerializable test = buildFrom(s);
		
		
		//Build the table
		SerializingTable table = new SerializingTable();
		table.addSerializer(new NetworkStringSerializer());
		table.addSerializer(new RecursiveTestSerializer());
		
		//Write the object
		buffer.clear();
		table.write(buffer, test);
		RecursiveTestSerializable result = (RecursiveTestSerializable) table.readNext(buffer, null);
		
		//Each object must be in order and equal to what was sent.
		for(int i = 0; i < result.strings.size(); i++){
			assertEquals(result.strings.get(i).value, s[i]);
		}
	}
	
	boolean receivedData = false;
	
	@Test
	public void serializerListenerTest(){
		String[] s = new String[]{"aaaaa", "bbbbb", "ccccc"};
		RecursiveTestSerializable test = buildFrom(s);
		
		SerializingTable table = new SerializingTable();
		table.addSerializer(new NetworkStringSerializer());
		table.addSerializer(new RecursiveTestSerializer());
		
		P2PMessageListener<NetworkString> stringListener = new P2PMessageListener<NetworkString>() {
			int receivedIndex = 0;
			String[] received = new String[]{"","",""};
			String[] expected = new String[]{"aaaaa", "bbbbb", "ccccc"};
			public void messageReceived(NetworkString message, MessageInfo info) {
				receivedData = true;
				received[receivedIndex] = message.value;
				assertEquals(received[receivedIndex], expected[receivedIndex]);
				receivedIndex++;
			}
		}; 
		
		table.getSerializer(NetworkStringSerializer.class).addListener(stringListener);
		
		//Write the object
		buffer.clear();
		table.write(buffer, test);
		table.readAll(buffer, null);
		
		assertTrue(receivedData);
	}

}
