package com.p2p;

import org.junit.Test;

import com.nativeMessages.Password;

import static org.junit.Assert.*;

public class NetworkInformationTest {
	
	private String name = "patate";
	
	@Test
	public void noPassword(){
		NetworkInformation info = new NetworkInformation(name, (short) 16);
		verify(info, name, 16, 0);
		info.addPeer();
		verify(info, name, 16, 1);
		//Should be able to connect
		assertTrue(info.canConnect());
		
		//Should now not be able to connect
		info.setAcceptingConnections(false);
		assertFalse(info.canConnect());
		
		//Should still not be able to connect
		info.setAcceptingConnections(true);
		info.addPeer(16);
		assertFalse(info.canConnect());
		
		//Still
		info.setAcceptingConnections(true);
		assertFalse(info.canConnect());
		
		verify(info, name, 16, 1+16);
	}
	
	@Test
	public void withPassword(){
		Password password = new Password("foofoo");
		Password anotherPassword = new Password("foo");
		NetworkInformation info = new NetworkInformation(name, (short)16, password);
		
		assertTrue(info.canConnect(password));
		assertFalse(info.canConnect(anotherPassword));
		
		info.setPassword(anotherPassword);
		
		assertFalse(info.canConnect(password));
		assertTrue(info.canConnect(anotherPassword));
		
		info.setAcceptingConnections(false);
		assertFalse(info.canConnect(anotherPassword));
		
		info.setAcceptingConnections(true);
		info.addPeer(16);
		assertFalse(info.canConnect(anotherPassword));
		
		
	}
	
	private void verify(NetworkInformation info, String requiredName
			, int maxPeers, int connectedPeers){
		assertEquals(requiredName, info.getName());
		assertEquals(maxPeers, info.getMaxConnectedPeers());
		assertEquals(connectedPeers, info.getConnectedPeers());
	}
}
