package com.backends;

public class FlushRequest {
	private short[] destinationIDs;
	
	public FlushRequest(){
	}
	
	public FlushRequest(short ... destinationIDs){
		this.destinationIDs = destinationIDs;
	}
	
	public short[] getDestinationIDs(){
		return destinationIDs;
	}
}
