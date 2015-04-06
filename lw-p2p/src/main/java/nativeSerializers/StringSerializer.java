package nativeSerializers;

import io.netty.buffer.ByteBuf;

import com.p2p.serializing.NetworkSerializer;
import com.p2p.serializing.NetworkString;
import com.p2p.serializing.SerializingTable;

public class StringSerializer extends NetworkSerializer<String> {

	@Override
	protected void write(ByteBuf stream, String object, SerializingTable table)
			throws RuntimeException {
		char[] value = object.toCharArray();
		for(char c : value){
			stream.writeChar(c);
		}
	}

	@Override
	protected String read(ByteBuf stream, int bytesToRead,
			SerializingTable table) throws RuntimeException {
		int numberOfChars = bytesToRead/2;//Two bytes per char
		char[] string = new char[numberOfChars];
		for(int i = 0; i < numberOfChars; i ++){
			string[i] = stream.readChar();
		}
		return String.valueOf(string);
	}

	@Override
	protected Class<String> getSerializableObjectType() {
		return String.class;
	}

}
