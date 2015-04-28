package com.backends;

import static org.junit.Assert.*;
import io.netty.channel.embedded.EmbeddedChannel;

import org.junit.Test;

import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.NewConnection;

public class ClientHandshakeHandlerTest implements HandshakeListener{

	private EmbeddedChannel channel;
	
	public ClientHandshakeHandlerTest(){
		channel = new EmbeddedChannel(new ClientHandshakeHandler(this));
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

	public void peersRequestingNewConnections(ConnectionAnswer answer,
			NewConnection thisConnection) {
		assertTrue(answer != null);
		assertTrue(answer.getNetworkInformation() != null);
		
	}

	public void connectionToNetworkSuccessful() {
		// TODO Auto-generated method stub
		
	}

	public void connectionToNetworkFailed() {
		// TODO Auto-generated method stub
		
	}

}
