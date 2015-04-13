package com.p2p;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.nativeMessages.Password;
import com.p2p.serializing.SerializingTable;
import com.p2p.serializing.SerializingTableBuilder;

public class NetworkAccess{
	public static final short DEFAULT_MAX_PEERS = 16;
	
	public static enum ConnectionStatus{unconnected, connected, connectedAsHost};
	private ConnectionStatus status;
	
	/*Client, Server and Network objects */
	private PeerClient client;
	private NettyServer server;
	private P2PNetwork network;
	
	public NetworkAccess(SerializingTable serialTable, int localPort) throws UnknownHostException{
		status = ConnectionStatus.unconnected;
		SerializingTableBuilder.appendRequiredSerializers(serialTable);
		server = new NettyServer(localPort, serialTable);
		client = new PeerClient(server);
		network = null;
	}
	
	public NetworkAccess createNewNetwork(String name, short maxPeers){
		network = initNetwork(name, maxPeers);
		status = ConnectionStatus.connectedAsHost;
		return this;
	}
	
	public NetworkAccess createNewNetwork(String name){
		return createNewNetwork(name, DEFAULT_MAX_PEERS);
	}
	
	public NetworkAccess setNetworkPassword(String password){
		network.getNetworkInfo().setPassword(new Password(password));
		return this;
	}
	
	public NetworkAccess connectToNetwork(InetSocketAddress networkTcpAddress){
		return this;
	}
	
	private P2PNetwork initNetwork(String name, short maxPeers){
		P2PNetwork network = new P2PNetwork();
		NetworkInformation info = new NetworkInformation(name, maxPeers);
		network.setNetworkInfo(info);
		network.addPeer(client);
		return network;
	}
		
	public ConnectionStatus getStatus(){
		return status;
	}
}
