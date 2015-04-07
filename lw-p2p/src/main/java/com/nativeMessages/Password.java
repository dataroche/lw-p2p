package com.nativeMessages;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {
	public static final byte[] EMPTY_HASH = new byte[]{0};
	
	byte[] hash;
	
	public Password(){
		setEmptyPassword();
	}
	
	public Password(byte[] hash){
		this.hash = hash;
	}
	
	public Password(String password){
		hash = hash(password);
	}
	
	private byte[] hash(String password){
		byte[] hash = null;
		if(password.equals(""))
			setEmptyPassword();
		else{
	        try {
	            MessageDigest md = MessageDigest.getInstance("SHA-1");
	            
	            hash = md.digest(password.getBytes());
	            
	        }
	        catch (NoSuchAlgorithmException e)
	        {
	            e.printStackTrace();
	        }
		}
        return hash;
	}
	
	public byte[] getHash(){
		return hash;
	}
	
	private void setEmptyPassword(){
		hash = EMPTY_HASH;
	}
	
	public boolean isEmptyPassword(){
		return hash.length == 1;
	}
	
	public boolean compare(Password otherPassword){
		boolean result = true;
		if(isEmptyPassword())
			return true;
		try{
			byte[] otherHash = otherPassword.getHash();
			for(int i = 0; i < hash.length; i++){
				
				if(hash[i] != otherHash[i])
					result = false;
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			result = false;
		}
		catch(NullPointerException e){
			result = false;
		}
		
		return result;
	}
	
}
