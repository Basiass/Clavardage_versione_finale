package Application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import Database.DatabaseController;

public class AcceptConversation extends Thread {
	
	private Utilisateur user;
	private Integer port = 51000 ;
	private Socket SocketLocal;
	private ServerSocket sockserv;
	
	public AcceptConversation (Utilisateur user) {
		this.user = user;
	}
	@Override
	public void run() {
		
		try {
			this.sockserv = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("ServerSocket couldn't be created");
			e.printStackTrace();
		}
		
		while(this.user.GetEtat()){
			try {
				
				// ACCEPTER DES DEMANDES DE CO QUI ST REDIRIGEES VERS UN AUTRE SOCKET
				this.SocketLocal= this.sockserv.accept();

				ObjectInputStream ois = new ObjectInputStream(this.SocketLocal.getInputStream());
				UtilisateurActif dest;
				
				dest = (UtilisateurActif)ois.readObject();
				
				Conversation Conv = new Conversation (this.SocketLocal, dest, this.user);
				
			} catch (IOException e) {
				System.err.println("wait");
				e.printStackTrace();
				
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
}
