package com.backends;

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
	
	public MessageInfo getInfo(){
		return info;
	}
	
	public ByteBuf getBuffer(){
		return buffer;
	}
}
