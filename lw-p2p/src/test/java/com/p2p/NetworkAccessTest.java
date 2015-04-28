package com.p2p;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import com.p2p.NetworkAccess.ConnectionStatus;
import com.p2p.serializing.SerializingTable;

public class NetworkAccessTest {

	private InetSocketAddress pseudoRemoteNetwork;
	private NetworkAccess accessReference;
	
	public NetworkAccessTest() throws UnknownHostException{
		accessReference = new NetworkAccess(new SerializingTable(), 5555);
		accessReference.createNewNetwork("test", (short)16);
		pseudoRemoteNetwork = new InetSocketAddress(InetAddress.getLocalHost(), 5555);
	}
	
	@Test
	public void statusChanges() throws UnknownHostException {
		
		NetworkAccess access = new NetworkAccess(new SerializingTable(), 8008);
		assertTrue(access.getStatus() == ConnectionStatus.unconnected);
		access.createNewNetwork("Test", (short)16);
		assertTrue(access.getStatus() == ConnectionStatus.connectedAsHost);
		//Reset
		access = new NetworkAccess(new SerializingTable(), 8008);
		access.connectToNetwork(pseudoRemoteNetwork);
		assertTrue(access.getStatus() == ConnectionStatus.connected);
	}

}
