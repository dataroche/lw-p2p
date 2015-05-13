package com.nativeRequests;

import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.NewConnection;

public class ConnectionBroadcastRequest {
	private NewConnection connection;
	private ConnectionAnswer answer;
	
	public NewConnection getConnection() {
		return connection;
	}
	public void setConnection(NewConnection connection) {
		this.connection = connection;
	}
	public ConnectionAnswer getAnswer() {
		return answer;
	}
	public void setAnswer(ConnectionAnswer answer) {
		this.answer = answer;
	}
	
}
