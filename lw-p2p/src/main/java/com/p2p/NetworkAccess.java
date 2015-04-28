package com.p2p;

import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.backends.HandshakeListener;
import com.backends.id.SocketId;
import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.NewConnection;
import com.nativeMessages.Password;
import com.p2p.serializing.SerializingTable;
import com.p2p.serializing.SerializingTableBuilder;

public class NetworkAccess implements HandshakeListener{
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
	
	public ChannelFuture connectToNetwork(InetSocketAddress networkTcpAddress){
		//TODO return some type of promise
		return null;
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

	public void peersRequestingNewConnections(ConnectionAnswer answer,
			NewConnection thisConnection) {
		network = new P2PNetwork(answer.getNetworkInformation());
		server.setP2PNetwork(network);
		
	}
	
	public void connectionToNetworkSuccessful() {
		// TODO Auto-generated method stub
		
	}

	public void connectionToNetworkFailed() {
		// TODO Auto-generated method stub
		
	}

}
