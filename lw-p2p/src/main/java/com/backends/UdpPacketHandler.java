package com.backends;


import java.net.InetSocketAddress;

import com.backends.id.SocketId;
import com.backends.id.SocketIdTable;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;

public class UdpPacketHandler extends ChannelDuplexHandler{

	private SocketIdTable table;
	
	public UdpPacketHandler(SocketIdTable idTable){
		table = idTable;
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		DatagramPacket receivedPacket = (DatagramPacket) msg;
		RawMessage message = new RawMessage();
		message.buffer = receivedPacket.content();
		message.info = decodeInfo(receivedPacket);
		
		//Fires the read for the RawMessage.
		super.channelRead(ctx, message);
	}
	
	private MessageInfo decodeInfo(DatagramPacket packet){
		SocketId senderId = table.getID(packet.sender());
		//Read the sender time stamp
		long timeStamp = packet.content().readLong();
		return new MessageInfo(senderId, timeStamp);
	}
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		if(!(msg instanceof MessageRequest)){
			System.err.println("Warning : object which is not of type MessageRequest got to the end of the outbound pipeline.");
			return;
		}
		
		MessageRequest request = (MessageRequest) msg;
		//Stamp the time.
		request.getBuffer().writeLong(
						System.currentTimeMillis());
		
		InetSocketAddress sender = table.getID(request.getClientID()).getUdpAddress();
		ByteBuf data = request.getBuffer();
		
		if(request.isBroadcast())
			broadcast(ctx, data, sender);
		else
			send(ctx, data, sender, request.getDestinationIDs());
		
		data.release();
	}
	
	private void broadcast(ChannelHandlerContext ctx, ByteBuf data, InetSocketAddress sender){
		for(SocketId id : table.getAll()){
			send(ctx, data, sender, id.getUdpAddress());
		}
		
	}
	
	private void send(ChannelHandlerContext ctx, ByteBuf data, InetSocketAddress sender, short ... ids){
		for(short id : ids){ 
			send(ctx, data, sender, table.getID(id).getUdpAddress());
		}
	}
	
	private void send(ChannelHandlerContext ctx, ByteBuf data, InetSocketAddress sender, InetSocketAddress recipient){
		ByteBuf copy = data.copy();
		DatagramPacket packet = new DatagramPacket(copy, recipient, sender);
		ctx.writeAndFlush(packet);
		copy.release();
	}

}