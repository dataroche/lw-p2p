package nettyTests;

import io.netty.example.discard.DiscardClient;
import io.netty.example.discard.DiscardServer;
import io.netty.example.echo.EchoClient;
import io.netty.example.echo.EchoServer;
import io.netty.example.telnet.TelnetClient;
import io.netty.example.telnet.TelnetServer;

public class Main implements Runnable{
	public static void main(String[] args) {
		Thread t = new Thread(new Main());
		t.start();
		try {
			TelnetServer.main(args);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void run() {
		try {
			TelnetClient.main(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
