package com.backends.id;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.backends.id.SocketId.Status;


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
		SocketId id = idMap.get(address);
		if(id == null){
			id = new SocketId((short) -999, address.getAddress(), address.getPort(), 0);
			id.idStatus = Status.UNRESOLVED;
			idMap.put(address, id);
		}
			
			
		return id;
	}
	
	public void identify(InetSocketAddress address, short assignedId, int udpPort){
		SocketId id = idMap.get(address);
		
		if(id == null){
			addMapping(new SocketId(assignedId, address.getAddress(), address.getPort(), udpPort));
		}
		else{
			id.clientId = assignedId;
			id.udpAddress = new InetSocketAddress(address.getAddress(), udpPort);
			addMapping(id);
		}
		id.idStatus = Status.IDENTIFIED;
	}
	
	
}
