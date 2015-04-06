package com.p2p;

import com.backends.MessagePacker;
import com.backends.MessageRequestIdAppender;
import com.p2p.serializing.SerializingChannelHandler;
import com.p2p.serializing.SerializingTable;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class P2PChannelInitializer extends ChannelInitializer<Channel> {

	private SerializingChannelHandler serialHandler;
	private short clientId;
	
	public P2PChannelInitializer(short clientId, SerializingTable table) {
		serialHandler = new SerializingChannelHandler(table);
		this.clientId = clientId;
	}
	
	public void init(Channel c) throws Exception{
		initChannel(c);
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		
		ch.pipeline().addLast(new MessagePacker())
					.addLast(new MessageRequestIdAppender(clientId))
					.addLast(serialHandler);
		
	}

}
