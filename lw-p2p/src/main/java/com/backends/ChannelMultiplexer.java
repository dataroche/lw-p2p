package com.backends;

import java.util.HashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;

public class ChannelMultiplexer extends ChannelOutboundHandlerAdapter {
	private HashMap<Short, Channel> channelMap;
	private ChannelGroup channelGroup;
}
