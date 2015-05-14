package com.p2p;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.Queue;

import org.junit.Test;

import com.backends.HandshakeHandler;
import com.backends.RawMessage;
import com.backends.TcpTestingPipeline;
import com.initializers.ClientChannelInitializer;
import com.initializers.ServerChannelInitializer;
import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.ConnectionAnswer.Answer;
import com.nativeMessages.ConnectionAttempt;
import com.nativeMessages.NewConnection;
import com.nativeMessages.Password;
import com.p2p.NettyServer;
import com.p2p.NetworkInformation;
import com.p2p.serializing.SerializingTable;

import io.netty.channel.embedded.EmbeddedChannel;

public class ServerPipelineTest {
	
	private NettyServer remoteServer;
	
	private EmbeddedChannel testServerChannel;
	
	public ServerPipelineTest() throws UnknownHostException{
		NetworkInformation info = new NetworkInformation("test", (short)16, new Password("foo"));
		remoteServer =  new NettyServer(new P2PNetwork(info), 1000);
		testServerChannel = new EmbeddedChannel(new HandshakeHandler(remoteServer));
	}
	
	private void writeToRemoteServer(Object o){
		RawMessage message = new RawMessage();
		message.addObject(o);
		testServerChannel.writeInbound(message);
	}
	
	private Queue<Object> readFromRemoteServer(){
		return testServerChannel.outboundMessages();
	}
	
	@Test
	public void respondsToConnectionAttemptWithNoPassword(){
		writeToRemoteServer(new ConnectionAttempt());
		//Should respond with a failed connection answer
		ConnectionAnswer answer = (ConnectionAnswer) readFromRemoteServer().poll();
		assertTrue(answer.getAnswer() == Answer.FAILURE);
	}
	
	@Test
	public void respondsToConnectionAttemptWithCorrectPassword(){
		writeToRemoteServer(new ConnectionAttempt("foo"));//correct password
		//Should respond with a success connection answer, and send a new connection.
		Queue<Object> queue = readFromRemoteServer();
		NewConnection connection = (NewConnection) queue.poll();
		ConnectionAnswer answer = (ConnectionAnswer) queue.poll();
		assertTrue(answer.getAnswer() == Answer.SUCCESS);
		assertTrue(connection.getAuthentificatorId().hashCode() == connection.hashCode());//Signature verification
	}
}
