package com.nativeMessages;

public class NewConnectionAcknowledge {
	private boolean accepted;
	
	public NewConnectionAcknowledge(boolean accepted){
		this.accepted = accepted;
	}
	
	public boolean isAccepted(){
		return accepted;
	}
}
