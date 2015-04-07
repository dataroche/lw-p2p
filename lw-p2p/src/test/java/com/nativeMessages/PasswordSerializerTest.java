package com.nativeMessages;

import static org.junit.Assert.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.junit.Test;

import com.nativeMessages.Password;
import com.nativeSerializers.PasswordSerializer;

public class PasswordSerializerTest {

	@Test
	public void readWriteEquals() {
		Password writePassword = new Password("blablabla");
		PasswordSerializer serializer = new PasswordSerializer();
		ByteBuf buffer = Unpooled.buffer();
		serializer.encode(buffer, writePassword);
		Password readPassword = serializer.decode(buffer, null);
		assertTrue(writePassword.compare(readPassword));
	}

}
