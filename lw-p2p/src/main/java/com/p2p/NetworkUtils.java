package com.p2p;

public class NetworkUtils {
	
	static long passwordHash(String password){
		return password.hashCode();
	}
	
}
