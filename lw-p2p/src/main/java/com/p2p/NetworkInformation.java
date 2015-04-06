package com.p2p;

import nativeMessages.Password;

public class NetworkInformation {
	
	
	private final String name;
	
	private final int maxConnectedPeers;
	
	private final boolean host;
	private final int hostID;
	
	
	private int connectedPeers;
	private boolean acceptingConnections;
	
	private boolean passwordProtected;
	private Password password;
	
	NetworkInformation(String name, int maxPeers, boolean host, int hostID){
		this.name = name;
		this.maxConnectedPeers = maxPeers;
		this.host = host;
		this.hostID = hostID;
		this.acceptingConnections = true;
		setWithNoPassword();
	}
	
	NetworkInformation(String name, int maxPeers, boolean host, int hostID, Password password){
		this(name, maxPeers, host, hostID);
		setPassword(password);
	}
	
	void setAcceptingConnections(boolean value){
		acceptingConnections = value;
	}
	
	void setWithNoPassword(){
		passwordProtected = false;
		password = new Password();
	}
	
	void setPassword(Password password){
		passwordProtected = true;
		this.password = password;
	}
	
	boolean verifyPassword(Password password){
		return passwordProtected ? this.password.compare(password) : true;
	}

	void addPeer(){
		connectedPeers++;
	}
	
	void addPeer(int number){
		connectedPeers += number;
	}
	
	public String getName() {
		return name;
	}

	public int getMaxConnectedPeers() {
		return maxConnectedPeers;
	}

	public boolean isPasswordProtected() {
		return passwordProtected;
	}

	public boolean isHost() {
		return host;
	}

	public int getHostID() {
		return hostID;
	}

	public int getConnectedPeers() {
		return connectedPeers;
	}

	public boolean isOpen() {
		return acceptingConnections && connectedPeers < maxConnectedPeers;
	}
	
	public boolean canConnect(){
		return canConnect(null);
	}
	
	public boolean canConnect(Password password){
		return isOpen() && verifyPassword(password);
	}
	
}
