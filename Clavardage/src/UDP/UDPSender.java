package UDP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;

import Application.UtilisateurActif;

public class UDPSender extends Thread{

    private static DatagramSocket socket = null;
    private UtilisateurActif me;
    private InetAddress bdr;
    private Boolean ChangerPseudo;
    
    // Envoyer sa présence à tous les utilisateurs du réseau.
    // MODE BROADCAST
    public UDPSender(UtilisateurActif me) throws SocketException {
        this.me = me;
        this.ChangerPseudo = false;
        ArrayList<InetAddress> liste = listAllBroadcastAddresses();
        this.bdr = liste.get(0);
        System.out.println("adr:"+this.bdr);
        this.socket = new DatagramSocket();
        try {
            socket.setBroadcast(true);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

   
    // UNICAST
    public UDPSender(UtilisateurActif me, InetAddress dest) throws SocketException {
        this.me = me;
        ArrayList<InetAddress> liste = listAllBroadcastAddresses();
        this.bdr = dest;
        this.socket = new DatagramSocket();
        this.ChangerPseudo = false;
    }

    // CHANGEMENT DE PSEUDO EN UNICAST
    public UDPSender(UtilisateurActif me, InetAddress dest, Boolean ChangerPseudo) throws SocketException {
        this.me = me;
        this.bdr = dest;
        this.socket = new DatagramSocket();
        this.ChangerPseudo = ChangerPseudo;
    }
 
    
    // retourne une liste d'adresse de broadcast
    public ArrayList<InetAddress> listAllBroadcastAddresses() throws SocketException {
         ArrayList<InetAddress> broadcastList = new ArrayList<>();
         Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
         while (interfaces.hasMoreElements()) {
             NetworkInterface networkInterface = interfaces.nextElement();
        
             if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
             }
        
             networkInterface.getInterfaceAddresses().stream()
                  .map(a -> a.getBroadcast())
                  .filter(Objects::nonNull)
                  .forEach(broadcastList::add);
             
        }
        return broadcastList;
   }

	
	
    public void run(){
   
    	ByteArrayOutputStream bStream = new ByteArrayOutputStream();
    	ObjectOutput oo = null ;
		try {
			oo = new ObjectOutputStream(bStream);
			if (!this.ChangerPseudo) {
    			oo.writeObject(this.me); 
    		} 
    		else {
    			oo.writeObject(this.me.GetPseudo());
    		} 
		} catch (IOException e1) {
			e1.printStackTrace();
		}

    	byte[] serializedMessage = bStream.toByteArray();
    	
        DatagramPacket packet = new DatagramPacket(serializedMessage, serializedMessage.length, this.bdr, 12345);
        
        try {
            socket.send(packet);
            oo.close();
        } catch (IOException e) {
           e.printStackTrace();
        }
        socket.close();
    }
}


