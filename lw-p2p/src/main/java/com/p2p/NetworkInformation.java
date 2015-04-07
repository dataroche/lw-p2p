package com.p2p;

import com.nativeMessages.Password;

/**
 * Contains all pertinent information about a linked P2PNetwork.
 * 
 * 
 * @author Will
 *
 */
public class NetworkInformation {
	
	
	private final String name;
	
	private final short maxConnectedPeers;
	
	private int connectedPeers;
	private boolean acceptingConnections;
	
	private boolean passwordProtected;
	private Password password;
	
	NetworkInformation(String name, short maxPeers){
		this.name = name;
		this.maxConnectedPeers = maxPeers;
		this.acceptingConnections = true;
		connectedPeers = 0;
		setWithNoPassword();
	}
	
	NetworkInformation(String name, short maxPeers, Password password){
		this(name, maxPeers);
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
	
	boolean isAcceptingConnections(){
		return acceptingConnections;
	}
	
	/**
	 * @return The hashed password needed to connect to this network.
	 */
	public Password getPassword(){
		return password;
	}
	
	/**
	 * @return The name associated with this network.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The maximum number of peers accepted by this network.
	 */
	public int getMaxConnectedPeers() {
		return maxConnectedPeers;
	}

	/**
	 * @return If this network is protected by a password.
	 */
	public boolean isPasswordProtected() {
		return passwordProtected;
	}

	/**
	 * @return The number of currently connected peers.
	 */
	public int getConnectedPeers() {
		return connectedPeers;
	}

	/**Checks if it is possible to connect to this network with the right password.
	 * @return If this network is accepting connections and that the connected peers do not exceed the maximum supported.
	 */
	public boolean isOpen() {
		return acceptingConnections && connectedPeers < maxConnectedPeers;
	}
	
	/**Checks if an empty password will succeed in connecting to this network.
	 * @return {@link isOpen()} and that the network is not protected by password. 
	 */
	public boolean canConnect(){
		return canConnect(null);
	}
	
	/**Checks if the specified password will succeed in connecting to this network.<br>
	 * If the network is not protected by password, no password check will occur.
	 * @param password
	 * @return {@link isOpen()} and that the password matches, if the network is protected by password. 
	 */
	public boolean canConnect(Password password){
		return isOpen() && verifyPassword(password);
	}
	
}
