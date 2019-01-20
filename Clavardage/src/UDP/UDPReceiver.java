package UDP;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
  import java.net.DatagramSocket;
  import java.net.InetAddress;
  import java.net.InetSocketAddress;
  import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import Application.Utilisateur;
import Application.UtilisateurActif;
import Views.mainWindowView;


public class UDPReceiver extends Thread {
	  private Utilisateur user;
      private static DatagramSocket socket = null;
      private UtilisateurActif me;
      private mainWindowView mw ;
      
      public UDPReceiver(Utilisateur user, UtilisateurActif me, mainWindowView mw) throws SocketException {
          this.user = user;
          UDPReceiver.socket = new DatagramSocket(12345);
          this.me = me;
          this.mw = mw ;
      }
   
      
      public void run() {
          boolean continuer = true ;
          
          while (this.user.GetEtat()) {
              byte[] buffer = new byte[1024];
              DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
              try {
            	  socket.receive(inPacket);
              } catch (IOException e) {
                  e.printStackTrace();
              }
              
              if (continuer == true ) {
                  continuer = false;
              }
              else {
                  Object readObject = null; 
                  UtilisateurActif expediteur = null;
                  String changementPseudo = null;
                  
                  
                  
                  try {
                      ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buffer));
                      readObject = iStream.readObject();
                    if (readObject instanceof UtilisateurActif) {
                        expediteur = (UtilisateurActif) readObject;
                    } else if (readObject instanceof String) { 
                    	changementPseudo = (String) readObject;
                    } else { 
                    	System.out.println("The received object is not of type String!");
                    }
                  } catch (Exception e) {
                	  System.out.println("No object could be read from the received UDP datagram.");
                  }

                  
                  
                  if (readObject!=null) {
                	  // Notification de départ ou d'arrivée
                	  
                	  if (expediteur != null) { 
                		  if (this.user.PresenceUtilisateurActif(expediteur) && expediteur.getDeco()) {
                			  // -> oui : il se déconnecte, le retirer de la liste des utilisateurs actifs
                			  this.user.SupprimerUserActif(expediteur);
                			  this.mw.deleteUser(expediteur);
                		  }
                		  else {
                			  // -> non : l'ajouter // + // REPONDRE SA PRESENCE
                			  this.user.AjouterUserActif(expediteur);    
                			  this.mw.addConnectedUser(expediteur);
                			  try {

                				  if (!expediteur.getDejaCo()) {
                					  UDPSender reponse = new UDPSender(this.me, inPacket.getAddress());
                					  reponse.start();
                					  this.me.setDejaCo(true);
                				  }

                			  } catch (SocketException e) {
                				  e.printStackTrace();
                			  }
                		  } 

                	  } else if (changementPseudo!=null) {
                		  // Quelqu'un a changé de pseudo
                		  // Mise à jour
                		  
                		  String oldPseudo = null;
                		  // RECUPERER L'ANCIEN PSEUDO
                		  for (UtilisateurActif useractif : this.user.GetListeUtilisateursActifs() ) {
                			  if (useractif.getInetAddress().equals(inPacket.getAddress())) {
                				  oldPseudo = useractif.GetPseudo();
                			  }
                		  }
                		  
                		  this.user.majPseudo(changementPseudo,inPacket.getAddress());
                		  this.mw.majPseudoDest(changementPseudo,oldPseudo);
             
                	  }


                  }

              }
          }
          socket.close();
      }

}