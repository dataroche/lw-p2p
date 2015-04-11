package com.backends;

import java.util.HashMap;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChannelMultiplexer {
	private HashMap<Short, Channel> channelMap;
	private ChannelGroup channelGroup;
	
	public ChannelMultiplexer(){
		channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		channelMap = new HashMap<Short, Channel>();
	}
	
	public void addChannel(short id, Channel c){
		channelGroup.add(c);
		channelMap.put(id, c);
	}
	
	public Channel getChannel(short id){
		return channelMap.get(id);
	}
	
	public ChannelGroup getAllChannels(){
		return channelGroup;
	}
}
