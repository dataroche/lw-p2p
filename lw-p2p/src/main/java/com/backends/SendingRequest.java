package com.backends;

public class SendingRequest {
	private short[] destinationIDs;
	public SendingRequest(short ... destinationIDs){
		this.destinationIDs = destinationIDs;
	}
	
	public short[] getDestinationIDs(){
		return destinationIDs;
	}
}
