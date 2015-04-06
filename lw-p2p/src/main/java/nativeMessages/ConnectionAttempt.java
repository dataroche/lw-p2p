package nativeMessages;

/**First message sent by the client attempting connection to one of the peer's server.
 * 
 * 
 * @author William Laroche
 *
 */
public class ConnectionAttempt {
	private Password passwordAttempt;
	private boolean requestPeerAddresses;
	public ConnectionAttempt(){
		passwordAttempt = new Password();
		requestPeerAddresses = false;
	}
	
	public ConnectionAttempt(String password){
		passwordAttempt = new Password(password);
		requestPeerAddresses = false;
	}
	
	public ConnectionAttempt(Password hashedPassword){
		passwordAttempt = hashedPassword;
		requestPeerAddresses = false;
	}
	
	public void doNotRequestPeerAddresses(){
		requestPeerAddresses = true;
	}
	
	public Password getPassword(){
		return passwordAttempt;
	}
	
	public boolean isRequestingPeerAddresses(){
		return requestPeerAddresses;
	}
}
