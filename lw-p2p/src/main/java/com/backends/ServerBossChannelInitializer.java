package com.backends;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ServerBossChannelInitializer extends ChannelInitializer<NioServerSocketChannel>{

	@Override
	protected void initChannel(NioServerSocketChannel ch) throws Exception {
		ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
		
	}

}
