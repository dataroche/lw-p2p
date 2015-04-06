package com.backends;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class MessageRequestIdAppender extends ChannelOutboundHandlerAdapter{
	private short localId;
	
	public MessageRequestIdAppender(short localId) {
		this.localId = localId;
	}
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		MessageRequest request =  (MessageRequest) msg;
		request.setClientID(localId);
		super.write(ctx, request, promise);
	}
}
