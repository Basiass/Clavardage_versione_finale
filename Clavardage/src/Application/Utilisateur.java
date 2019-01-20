 package Application;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Scanner;

import Database.DatabaseController;
import UDP.UDPReceiver;
import UDP.UDPSender;
import Views.ConnectionView;
import Views.PseudoChangeView;
import Views.mainWindowView;


public class Utilisateur {
	
	
	private String identifiant; 
	private String pseudo;
	private Boolean etat ; 		// vrai si connecté
	private InetAddress adrIP;
	
	private ArrayList<UtilisateurActif> listeUsersActifs;
	
	public Utilisateur(String pseudo, String Identifiant) {
		this.pseudo = pseudo;
		this.identifiant = Identifiant;
		this.etat = true ; 
		
		Enumeration<NetworkInterface> e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements())
			{
			    NetworkInterface n = (NetworkInterface) e.nextElement();
			    Enumeration<InetAddress> ee = n.getInetAddresses();
			    while (ee.hasMoreElements())
			    {
			        InetAddress i = (InetAddress) ee.nextElement();
			        if (!i.isLoopbackAddress() && (i.getHostAddress().length()<20)) {
			        	this.adrIP = i;
			        }
			        
			    }
			}
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println(this.adrIP.getHostAddress());
	   
		// Création de la liste
		this.listeUsersActifs = new ArrayList <UtilisateurActif> (); 	
		
	}


	/*
	 * Envoie une notification de présence UDP 
	 */
	
	public void SeConnecter(UtilisateurActif me) {
		UDPSender presence;
		try {
			presence = new UDPSender(me);
			presence.start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	/*
	 *	Vérifie dans la liste des UtilisateurActif si le pseudo est déjà utilisé
	 * 	Renvoie true s'il est libre, false sinon
	 */
	
	public boolean VerifierPseudoDispo (String pseudoEntre) {
		Boolean utilise = true;
		for ( UtilisateurActif useractif : listeUsersActifs) {
			if (useractif.GetPseudo().equals(pseudoEntre)) {
				utilise = false;
				break;
			}
		}
		return utilise ;
	}
	
	/*
	 * Met à jour notre propre pseudo
	 */
	
	public void ChangementPseudo(UtilisateurActif me,String pseudoEntre) {
		SetPseudo(pseudoEntre);
		me.setPseudo(pseudoEntre);
		System.out.println("Mon nouveau psuedo ok  size  " + listeUsersActifs.size());

		UDPSender Send_changement;
		// envoyer nouveau pseudo à toute la liste
		for (int j =0; j<listeUsersActifs.size(); j++) {
			try {
				Send_changement = new UDPSender(me, listeUsersActifs.get(j).getInetAddress(), true);
				Send_changement.start();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	
	/*
	 * 	Change le pseudo de l'UtilisateurActif dans la liste des UtilisateurActif 
	 */
	
	public void majPseudo (String nouveauPseudo, InetAddress addr) {
		for (UtilisateurActif useractif : GetListeUtilisateursActifs() ) {
			System.out.println(addr.getHostAddress()+ "   "+ useractif.getInetAddress().getHostAddress());
			  if (addr.equals(useractif.getInetAddress())) {
				  useractif.setPseudo(nouveauPseudo);
				  System.out.println("nouveau pseudo mis en place" + nouveauPseudo);
				  break;
			  }
		  }
	}
	
	/*
	 * Envoie un message de deconnection via UDP
	 */
	
	public void SeDeconnecter(UtilisateurActif me) {
        
        UDPSender depart;
           try {    
               this.etat = false;
               // envoyer à tout le monde une notification de départ
               for (int i =0; i<listeUsersActifs.size(); i++) {
                   depart = new UDPSender(me, listeUsersActifs.get(i).getInetAddress());
                   depart.start();
                   System.out.println("déco envoyé à " + listeUsersActifs.get(i).GetPseudo());
               }
           } catch (SocketException e) {
               System.out.println("error");
               e.printStackTrace();
           }
	}
   
	/*
	 * Ajoute un nouvel UtilisateurActif dans la liste des UtilisateurActif
	 */
	
	public void AjouterUserActif(UtilisateurActif useractif){
		listeUsersActifs.add(useractif);
		System.out.println("utilisateur ajouté"+ useractif);
		System.out.println("AFFICHAGE LISTE");
		for (UtilisateurActif us : this.listeUsersActifs) {
			System.out.println(us.GetIdentifiant() +"  "+ us.GetPseudo() + "  "+ us.getInetAddress().getHostAddress() );
		}
	}
	
	/*
	 * Supprimer un UtilisateurActif de la liste suite à une deconnection
	 */
	
	public void SupprimerUserActif(UtilisateurActif userToDelete){		
		for (UtilisateurActif userActif : listeUsersActifs)
		{
			if (userActif.GetIdentifiant().equals(userToDelete.GetIdentifiant())) { listeUsersActifs.remove(userActif);break;}
		}
		
		System.out.println("AFFICHAGE LISTE");
		for (UtilisateurActif us : this.listeUsersActifs) {
			System.out.println(us.GetIdentifiant() +"  "+ us.GetPseudo() + "  "+ us.getInetAddress().getHostAddress() );
		}
	}
	
	
	/*
	 *	Renvoie l'état d'activité d'un UtilisateurActif 
	 */
	
	public boolean  PresenceUtilisateurActif (UtilisateurActif us1) {
		boolean actif = false;
		for (UtilisateurActif userActif : listeUsersActifs)
		{
			if (userActif.GetIdentifiant().equals(us1.GetIdentifiant())) {actif = true ;break;}
		}
		return actif ;
	}

	
	public void SetPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	
	
	public Boolean GetEtat() {
		return etat;
	}
	
	public String getIdentifiant() {
		return identifiant;
	}
	
	public String getPseudo() {
		return pseudo;
	}
	
    public InetAddress getAdrIP() {
        return adrIP;     
    } 
	
	public ArrayList<UtilisateurActif> GetListeUtilisateursActifs(){
		return this.listeUsersActifs;
	}

	
    
	public static void main(String[] args) {
		
		DatabaseController.createDatabase();
		ConnectionView connectionView = new ConnectionView();
		
		boolean boucle = true ;
		while (boucle) {
			boucle = !connectionView.getIdentificationVerifiee();
			System.out.println();
		}
		System.out.println("Identification Vérifiée, début de la session de clavardage");
		
		
		Utilisateur user = new Utilisateur(connectionView.getIdentifiant(),connectionView.getIdentifiant());
		UtilisateurActif moi = new UtilisateurActif(user.getPseudo(), user.getIdentifiant(),user.getAdrIP(),false,false);
		
		mainWindowView mainWindow = new mainWindowView (user,moi);
		
		UDPReceiver SocketBdr;
		try {
			SocketBdr = new UDPReceiver (user, moi, mainWindow);
	        SocketBdr.start();
	        user.SeConnecter(moi);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		

		// Lancement du thread d'acceptation de conversations
		AcceptConversation acceptConv = new AcceptConversation(user);
		acceptConv.start();

		Date d = new Date();
		System.out.println(d + "||" + d.getTime() + new Date());
	}
	
}
