package com.p2p;

import static org.junit.Assert.*;

import org.junit.Test;

public class P2PNetworkTest {

	@Test
	public void networkInfoAlwaysValid() {
		P2PNetwork network = new P2PNetwork();
		assertTrue(network.getNetworkInfo() != null);
	}

}
