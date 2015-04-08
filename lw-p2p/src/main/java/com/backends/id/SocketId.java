package com.backends.id;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class SocketId {
	public static enum Status{UNRESOLVED, IDENTIFY_PENDING, IDENTIFIED}
	Status idStatus;
	String name;
	short clientId;
	InetSocketAddress udpAddress;
	InetSocketAddress tcpAddress;
	
	public SocketId(short id, InetAddress ipAddress, int tcpPort, int udpPort){
		name = "" + clientId;
		udpAddress = new InetSocketAddress(ipAddress, udpPort);
		tcpAddress = new InetSocketAddress(ipAddress, tcpPort);
		clientId = id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * @return The associated name to this socket. Default is the id.
	 */
	public String getName(){
		return name;
	}

	public short getClientId() {
		return clientId;
	}

	public InetSocketAddress getUdpAddress() {
		return udpAddress;
	}

	public InetSocketAddress getTcpAddress() {
		return tcpAddress;
	}
	
	public boolean isIdentified(){
		return idStatus.equals(Status.IDENTIFIED);
	}
	
	@Override
	public int hashCode() {
		
		return udpAddress.hashCode() + tcpAddress.hashCode() + 7 * clientId;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equals = true;
		SocketId other = (SocketId) obj;
		if(!udpAddress.equals(other.udpAddress))
			equals = false;
		if(!tcpAddress.equals(other.tcpAddress))
			equals = false;
		
		if(clientId != other.clientId){
			if(equals)
				System.err.println("Warning : Two socketIds with the same addresses but different ids.");
			equals = false;
		}
			
		
		return equals;
	}
}
