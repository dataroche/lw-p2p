package com.backends.id;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


public class SocketIdTable {
	private Map<Short, SocketId> addressMap;
	private Map<InetSocketAddress, SocketId> idMap;
	
	public SocketIdTable(){
		addressMap = new HashMap<Short, SocketId>();
		idMap = new HashMap<InetSocketAddress, SocketId>();
	}
	
	public void addMapping(SocketId socket){
		//Map the short id to the id.
		addressMap.put(socket.getClientId(), socket);
		
		//Map both udp and tcp addresses to the id.
		idMap.put(socket.getTcpAddress(), socket);
		idMap.put(socket.getUdpAddress(), socket);
	}
	
	public SocketId getID(short id){
		return addressMap.get(id);
	}
	
	public SocketId getID(InetSocketAddress address){
		return idMap.get(address);
	}
	
	
}
