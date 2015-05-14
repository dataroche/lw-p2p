package com.backends;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Queue;

import io.netty.channel.embedded.EmbeddedChannel;

import org.junit.Test;

import com.backends.MessagePacker.EmptyMessageRequestException;
import com.nativeRequests.FlushRequest;

public class MessagePackerTest {

	private MessagePacker packer = new MessagePacker();
	private EmbeddedChannel channel = new EmbeddedChannel(packer);
	
	@Test(expected = EmptyMessageRequestException.class)
	public void emptyMessageExceptionWithRequest() {
		channel.write(new FlushRequest());
	}
	
	@Test
	public void noEmptyMessageExceptionWithFlush() {
		channel.flush();
	}
	
	@Test
	public void oneObjectConservation(){
		String test = "Hello";
		channel.write(test);
		channel.flush();
		Queue<Object> messages = channel.outboundMessages();
		assertTrue(messages.size() == 1);
		MessageRequest request = (MessageRequest) messages.poll();
		
		assertTrue(request.getBuffer() != null);
		assertTrue(request.requests().iterator().next().equals(test));
	}
	
	@Test
	public void multipleObjectConservation(){
		String[] test = {"Hello", "Bob", "Georges"};
		channel.write(test[0]);
		channel.write(test[1]);
		channel.write(test[2]);
		FlushRequest flush = new FlushRequest((short)0, (short)1);
		channel.write(flush);
		Queue<Object> messages = channel.outboundMessages();
		assertTrue(messages.size() == 1);
		MessageRequest request = (MessageRequest) messages.poll();
		
		assertTrue(request.getBuffer() != null);
		Iterator<Object> it = request.requests().iterator();
		for(int i = 0; i < 3; i++){
			assertTrue(it.next().equals(test[i]));
		}
		assertTrue(request.getDestinationIDs()[0] == 0);
		assertTrue(request.getDestinationIDs()[1] == 1);
	}
	
	@Test
	public void multipleRequests() {
		multipleObjectConservation();
		multipleObjectConservation();
		multipleObjectConservation();
	}

}
