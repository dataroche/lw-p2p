package com.backends;


import com.backends.id.SocketId;
import com.backends.id.SocketIdTable;

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
		MessageRequest request = (MessageRequest) msg;
		//Stamp the time.
		request.getBuffer().writeLong(
						System.currentTimeMillis());
		
		SocketId sender = table.getID(request.getClientID());
		
		//Send the data to all destination ids.
		for(short id : request.getDestinationIDs()){
			SocketId recipient = table.getID(id);
			DatagramPacket packet = new DatagramPacket(request.getBuffer(), recipient.getUdpAddress(), sender.getUdpAddress());
			super.write(ctx, packet, promise);
		}
	}

}