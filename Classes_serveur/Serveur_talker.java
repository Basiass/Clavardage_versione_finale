package Serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Application.UtilisateurActif;

public class Serveur_talker extends Thread{
	
	private static UtilisateurActif user = null;
	private Serveur_talker serveur;
	
	public Serveur_talker (UtilisateurActif moi) {
		this.user = moi;
	}
	
	
	public static ArrayList<UtilisateurActif> Connection() {
		ArrayList<UtilisateurActif> liste = null;
		System.out.println("Pseudo envoyé" + user.GetPseudo());
		String requete = "http://10.1.5.233:8080/Serveur_servlet/Servlet?maj=0&deconnection=1&pseudo=" + user.GetPseudo() + "&identifiant=" + user.GetIdentifiant() + "&dejaCo=1";
		liste = communication_liste(requete);
		return liste;
	}
	
	
	public static void Deconnection() {
		String requete = "http://10.1.5.233:8080/Serveur_servlet/Servlet?maj=0&deconnection=0&pseudo=" + user.GetPseudo() + "&identifiant=" + user.GetIdentifiant() + "&dejaCo=0";
		URL url = null;
		try {
			url = new URL(requete);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		URLConnection conn = null;
		try {
			conn = url.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
	
	public static void ChangementPseudo() {
		String requete = "http://10.1.5.233:8080/Serveur_servlet/Servlet?maj=0&deconnection=1&pseudo=" + user.GetPseudo() + "&identifiant=" + user.GetIdentifiant() + "&dejaCo=1";
		URL url = null;
		try {
			url = new URL(requete);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		URLConnection conn = null;
		try {
			conn = url.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
	// a faire toutes les 5 secondes
	public static ArrayList<UtilisateurActif> maj() {
		String requete = "http://10.1.5.233:8080/Serveur_servlet/Servlet?maj=1&deconnection=1&pseudo=" + user.GetPseudo() + "&identifiant=" + user.GetIdentifiant() + "&dejaCo=1";
		ArrayList<UtilisateurActif> liste = null;
		liste = communication_liste(requete);
		return liste;
	}
	
	
	public static ArrayList<UtilisateurActif> communication_liste(String requete) {
		ArrayList<UtilisateurActif> liste = null;
		URL url = null;
		try {
			url = new URL(requete);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		URLConnection conn = null;
		BufferedReader in = null;
		String inputLine = null;
		try {
			conn = url.openConnection();
			in = new BufferedReader(new InputStreamReader (conn.getInputStream()));
			inputLine = in.readLine();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		UtilisateurActif nouveau = null;
		String pseudo = null;
		String identifiant = null;
		InetAddress adrip = null;
		String carac = "0";
		int numero = 1;
		int attribut = 1;
		int lettre = 1;

			// LECTURE CARACTERE PAR CARACTERE
			// Format : ,pseudo,identifiant,adrip ; ... ; §
			// A VERIFIER
		
		if (inputLine != null) {
			System.out.println("input line :   " +inputLine);
			while (carac != "§") {
				while (carac != ";") {
					
					while (!carac.equals(",")) {
						lettre++;
						carac = inputLine.substring(lettre-1,lettre);
					}
					
					if (numero == 1) {
						pseudo = inputLine.substring(attribut, lettre-1);
						numero ++;
					}
					if (numero == 2) {
						identifiant = inputLine.substring(attribut, lettre-1);
						if (identifiant.equals(user.GetIdentifiant()))
							break;
						numero++;
					}
					if (numero == 3) {
						try {
							System.out.println("ip1 : " + inputLine.substring(attribut, lettre-1));
							adrip = InetAddress.getByName(inputLine.substring(attribut +1, lettre-1));
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					attribut = lettre +1;
					System.out.println(" ip: " + adrip);
					nouveau = new UtilisateurActif(pseudo, identifiant, adrip, true, false);
					liste.add(nouveau);
					nouveau = null;
					numero = 1;
				}
			}
		}
		return liste;
	}
	

}
