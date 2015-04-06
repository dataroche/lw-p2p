package nativeSerializers;

import nativeMessages.Password;
import io.netty.buffer.ByteBuf;

import com.p2p.serializing.NetworkSerializer;
import com.p2p.serializing.SerializingTable;

public class PasswordSerializer extends NetworkSerializer<Password>{

	@Override
	protected void write(ByteBuf stream, Password object, SerializingTable table) {
		byte[] bytes = object.getHash();
		for(byte b : bytes){
			stream.writeByte(b);
		}
	}

	@Override
	protected Password read(ByteBuf stream, int bytesToRead,
			SerializingTable table) {
		byte[] passwordHash = new byte[bytesToRead];
		for(int i = 0; i < bytesToRead; i++){
			passwordHash[i] = stream.readByte();
		}
		return new Password(passwordHash);
	}

	@Override
	protected Class<Password> getSerializableObjectType() {
		return Password.class;
	}

}
