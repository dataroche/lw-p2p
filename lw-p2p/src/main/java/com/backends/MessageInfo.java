package com.backends;

import com.backends.id.SocketId;

public class MessageInfo{
	
	private long timeStamp;
	
	private SocketId senderId;
	
	public MessageInfo(SocketId senderId, long timeStamp){
		this.timeStamp = timeStamp;
		this.senderId = senderId;
	}
	
	public long getTimeStamp(){
		return timeStamp;
	}

	public SocketId getSenderID() {
		return senderId;
	}
}
