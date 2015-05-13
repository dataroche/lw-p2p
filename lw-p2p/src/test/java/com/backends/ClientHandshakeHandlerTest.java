package com.backends;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

import io.netty.channel.embedded.EmbeddedChannel;

import org.junit.Test;

import com.backends.id.SocketId;
import com.nativeMessages.ConnectionAnswer;
import com.nativeMessages.ConnectionAnswer.Answer;
import com.nativeMessages.NetworkWelcome;
import com.nativeMessages.NewConnection;
import com.p2p.NetworkInformation;

public class ClientHandshakeHandlerTest implements HandshakeListener{

	private EmbeddedChannel channel;
	private ClientHandshakeHandler handler = new ClientHandshakeHandler(this);
	private NetworkInformation info = new NetworkInformation("Bob", (short) 16);
	
	
	private NewConnection connection = new NewConnection(new SocketId(), new SocketId(), new SocketId().hashCode(), (short) 1);
	
	private ArrayList<Object> list = new ArrayList<Object>();
	
	public ClientHandshakeHandlerTest(){
		channel = new EmbeddedChannel(handler);
	}
	
	private void addPeersToNetwork(int count){
		String methodName = "addPeer";
		try {
			Method m = NetworkInformation.class.getDeclaredMethod(methodName, int.class);
			m.setAccessible(true);
			m.invoke(info, count);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void successfulConnectionWithNewConnection(){
		
		//No peers in the network, the connection should be successful.
		expectingAnswer = true;
		expectingSuccess = true;
		expectingFailure = false;
		simplePositiveAnswer();
		
		if(!passedAnswer)
			fail("Did not contact listener after new connections");
		
		
	}
	
	private void simplePositiveAnswer(){
		ConnectionAnswer answer = new ConnectionAnswer(Answer.SUCCESS);
		answer.setNetworkInformation(info);
		
		RawMessage message = new RawMessage();
		message.objects = list;
		
		list.add(answer);
		list.add(connection);
		channel.writeInbound(message);
	}
	
	@Test
	public void contactsAfterReceivingAllWelcomes(){
		addPeersToNetwork(1);//one fake peer, will expect one welcome, so the connection will not be immediately successful
		simplePositiveAnswer();
		
		RawMessage message = new RawMessage();
		message.objects = list;
		
		list.clear();
		list.add(new NetworkWelcome());
		
		expectingAnswer = false;
		expectingSuccess = true;
		expectingFailure = false;
		channel.writeInbound(message);
		
		if(!passedSuccess)
			fail("Expecting the success of the connection, since only 1 peer is connected to the network.");
	}
	
	
	private boolean expectingAnswer;
	private boolean passedAnswer = false;

	public void connectionAccepted(NetworkInformation info) {
		if(expectingAnswer){
			assertTrue(info != null);
		}
		else
			fail("No action was expected.");
		
		passedAnswer = true;
		
	}

	private boolean expectingSuccess;
	private boolean passedSuccess = false;
	public void connectionSuccessful() {
		if(!expectingSuccess)
			fail("Success not expected.");
		
		passedSuccess = true;
	}

	private boolean expectingFailure;
	private boolean passedFailure = false;
	public void connectionFailed() {
		if(!expectingSuccess)
			fail("Failure not expected.");
		
		passedFailure = true;
	}

}
