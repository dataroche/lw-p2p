package com.backends;

import java.net.InetSocketAddress;

import com.backends.id.SocketIdTable;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class TcpPacketHandler extends ChannelDuplexHandler{
	
	private SocketIdTable table;
	
	public TcpPacketHandler(SocketIdTable idTable){
		table = idTable;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buffer = (ByteBuf) msg;
		short senderId = buffer.readShort();
		long timeStamp = buffer.readLong();
		RawMessage message = new RawMessage();
		message.buffer = buffer;
		message.info = new MessageInfo(table.getID(senderId), timeStamp);
		
		InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		
		super.channelRead(ctx, message);
	}
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		MessageRequest request = (MessageRequest) msg;
		short thisId = request.getClientID();
		
		for(short destinationIds: request.getDestinationIDs()){
			
		}
	}
}
