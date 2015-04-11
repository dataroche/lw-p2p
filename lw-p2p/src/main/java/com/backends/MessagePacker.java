package com.backends;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class MessagePacker extends ChannelOutboundHandlerAdapter {
	
	public static class EmptyMessageRequestException extends Exception{


		/**
		 * 
		 */
		private static final long serialVersionUID = -6447869712740788612L;
		public EmptyMessageRequestException(){
			super("Requesting to send a message with no content.");
		}
	}

	
	private MessageRequest currentRequest;
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		if(msg instanceof FlushRequest){
			sendingRequest(ctx, (FlushRequest) msg, promise);
		}	
		else{
			if(isCurrentRequestEmpty()){
				currentRequest = new MessageRequest();
				currentRequest.setBuffer(ctx.alloc().buffer());
			}
			currentRequest.add(msg);
		}
	}
	
	private void sendingRequest(ChannelHandlerContext ctx, FlushRequest request,
			ChannelPromise promise) throws Exception, EmptyMessageRequestException{
		if(isCurrentRequestEmpty())
			throw new EmptyMessageRequestException();
		
		currentRequest.setDestinationIDs(request.getDestinationIDs());
		ctx.writeAndFlush(currentRequest, promise);
		
	}
	
	private boolean isCurrentRequestEmpty(){
		return currentRequest == null;
	}
	
	@Override
	public boolean isSharable() {
		return false;
	}
}
