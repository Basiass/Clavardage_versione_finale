package Application;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;


public class ConversationListener extends Thread{

	private Socket sock;
	private InetAddress ipToSend;
	private Utilisateur user;
	private Conversation conv;
	private boolean continuer = true ;
	
	public ConversationListener(Utilisateur user, Socket SocketLocal, Conversation conv) {
		this.sock = SocketLocal ;
		this.ipToSend = SocketLocal.getInetAddress();
		this.user=user;
		this.conv = conv ;
	}
	
	public void run() {
		
		boolean fermerSocket = true ;
		
		String message_distant = "";
		
		while (continuer) {
		  try {
	
	            BufferedReader input = new BufferedReader (new InputStreamReader(sock.getInputStream()));
	            
	            message_distant = input.readLine();
	   
	            // Quand l'interlocuteur ferme la fenêtre
			  	if (message_distant == null || message_distant.equals("close")) {
			  		this.continuer = false ;
					this.conv.getConvView().desactiverSendBtn();
			  	} else {
	            conv.receiveMessage(message_distant);
			  	}
	        
	        } catch (UnknownHostException ex) {
	 
	            System.out.println("Server not found: " + ex.getMessage());
	 
	        }catch (SocketException ex ) {
	        	
	        	fermerSocket = false ;
	        	continuer = false ;
	        	this.conv.getConvView().fermerConversation();
	        	try {
					this.sock.close();
					System.out.println("fermeture du socket");
				} catch (IOException e) {
					e.printStackTrace();
				}
	        	
	        } catch (IOException ex) {
				ex.printStackTrace();
	        } 
		  	
		  	
		}
		
		if (fermerSocket) { 
			try {
				this.sock.close();
				System.out.println("fermeture du socket");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Quand on appuie sur exit on appelle cette fonction
	public void terminerListener () {
		if ( this.continuer == true ) {
			this.continuer = false;
			this.conv.sendMessage("close");
		}
	}
}
