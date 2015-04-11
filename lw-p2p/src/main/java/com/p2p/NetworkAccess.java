package com.p2p;

import java.net.InetSocketAddress;

public class NetworkAccess{
	public static enum ConnectionStatus{unconnected, connected, connectedAsHost};
	private ConnectionStatus status;
	
	/*Client, Server and Network objects */
	private NettyClient client;
	private NettyServer server;
	private P2PNetwork network;
	
	public NetworkAccess(){
		status = ConnectionStatus.unconnected;
	}
	
	private void initClient(){
		
	}
	
	public NetworkAccess createNewNetwork(){
		return this;
	}
	
	public NetworkAccess setNetworkPassword(String password){
		return this;
	}
	
	public NetworkAccess connectToNetwork(InetSocketAddress networkTcpAddress){
		return this;
	}
	
	public ConnectionStatus getStatus(){
		return status;
	}
}
