package com.backends;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.backends.MessageInfo;
import com.backends.RawMessage;
import com.backends.id.SocketId;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Transforms a ByteBuf in a RawMessage object.
 * 
 * @author Will
 *
 */
public class RawMessageBuilder extends ChannelInboundHandlerAdapter{
	
	private SocketId id;
	private int transportDelay;
	
	public RawMessageBuilder(short remoteId, int localPort, int transportDelay){
		try {
			id = new SocketId(remoteId, InetAddress.getLocalHost(), localPort, localPort + 1);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.transportDelay = transportDelay; 
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		RawMessage message = new RawMessage();
		message.buffer = (ByteBuf) msg;
		message.info = new MessageInfo(id, System.currentTimeMillis() - transportDelay);
		ctx.fireChannelRead(message);
	}
}
