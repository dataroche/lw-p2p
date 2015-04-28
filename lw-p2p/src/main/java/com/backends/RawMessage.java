package com.backends;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.socket.DatagramPacket;


/**
 * Package data structure. Contains a byte buffer and message info.
 * 
 * @author William Laroche
 *
 */
public class RawMessage {
	MessageInfo info;
	ByteBuf buffer;
	List<Object> objects;
	
	public RawMessage(){
		objects = new LinkedList<Object>();
	}
	
	public void addObject(Object o){
		objects.add(o);
	}
	
	public MessageInfo getInfo(){
		return info;
	}
	
	public ByteBuf getBuffer(){
		return buffer;
	}
	
	public List<Object> getObjects(){
		return objects;
	}
	
	/**
	 * Searches for the first instance of the objectType.
	 * @param objectType The Class of the desired object.
	 * @return The first object of type objectType found, or null if none were found.
	 */
	@SuppressWarnings("unchecked")
	public <Type> Type searchFor(Class<Type> objectType){
		for(Object o : getObjects()){
			if(objectType.isInstance(o))
				return (Type) o;
		}
		return null;
	}
}
