package com.p2p.serializing;

import java.net.SocketAddress;

import com.backends.RawMessage;
import com.backends.MessageRequest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class TcpSerializingChannelHandler extends ChannelDuplexHandler{

	private SerializingTable serialTable;
	
	public TcpSerializingChannelHandler(SerializingTable table){
		serialTable = table;
	}

	
	/*CHANNEL OUTBOUND HANDLER*******************************************************/
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		MessageRequest request = (MessageRequest) msg;
		ByteBuf buffer = request.getBuffer();
		for(Object o : request.requests())
			if(serialTable.canWrite(o))
				serialTable.write(buffer, o);
		
		ctx.write(request, promise);
	}

	
	/*CHANNEL INBOUND HANDLER********************************************************/
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		RawMessage message = (RawMessage) msg;
		ByteBuf buffer = message.getBuffer();
		
		while(serialTable.canRead(buffer))
			message.addObject(serialTable.readNext(buffer, message.getInfo()));
		
		ctx.fireChannelRead(message);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
	}
	
}
