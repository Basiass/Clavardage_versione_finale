package Application;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

import Views.ConversationView;
import Views.mainWindowView;

public class Conversation extends Thread {
	
	private Socket sock;
	private Utilisateur user;
	private Integer port = 51000;
	private UtilisateurActif dest ;
	private ConversationView convView ;
	private ConversationListener listener;
	
	private mainWindowView mwv;
	
	public Conversation(Socket sock,UtilisateurActif dest, Utilisateur emetteur ) {
		this.sock = sock ;
		this.dest = dest ;
		this.user = emetteur ;
		convView = new ConversationView (this);
		convView.afficherAnciensMessages();
		listener = new ConversationListener(this.user,sock,this);
		listener.start();

	}

	
	public Conversation (Utilisateur user ) {
		this.user = user ;
		convView = new ConversationView (this);
		
	}
	
	
	public void initiate (UtilisateurActif dest, mainWindowView mwv){
		
		this.dest = dest ;
		convView.afficherAnciensMessages();
		
		try {
			

			sock = new Socket(dest.getInetAddress().getHostAddress(),port);
			listener = new ConversationListener(this.user,sock,this);
			listener.start();
			ObjectOutputStream oos = new ObjectOutputStream(this.sock.getOutputStream());
			oos.writeObject(new UtilisateurActif(user.getPseudo(), user.getIdentifiant(),user.getAdrIP(),true,false));
			
		} catch (IOException e) {
			mwv.afficherFenetreUtilisateurDeconnecte(this);
			convView.desactiverAffichageFrame();
		}
		
		
	}

	public void sendMessage(String contenu) {
		PrintWriter out;
		
		try {
			out = new PrintWriter (this.sock.getOutputStream(),true);
			out.println(contenu);
	    	out.flush();
		}catch (SocketException sockEx) {
			convView.desactiverSendBtn();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public ConversationView getConvView () {
		return this.convView ;
	}
	
	public void receiveMessage(String contenu) {
		convView.receive(contenu);
	}

	
	public void terminerConversation () {
		listener.terminerListener();
	}

	public UtilisateurActif getDest() {
		return this.dest ;
	}
	
	public Utilisateur getUtilisateur() {
		return this.user ;
	}
	
	public void seDeconnecter () {
			convView.fermerConversation();
	}
	

}
