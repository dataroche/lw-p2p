package com.backends;

import com.nativeMessages.ConnectionAnswer;
import com.nativeRequests.ConnectionBroadcastRequest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Handler overview : <br>
 * Checks for outgoing {@link ConnectionBroadcastRequest}<br>
 * if found -><br>
 * Sends correct NewConnections to all requesting peers<br>
 * 
 * @author WILL
 *
 */
public class ClientHandshakeOutboundHandler extends ChannelOutboundHandlerAdapter{
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		if(msg instanceof ConnectionBroadcastRequest){
			
		}
	}
}
