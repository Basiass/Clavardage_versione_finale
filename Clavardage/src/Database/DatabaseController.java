package Database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import Application.UtilisateurActif;


public class DatabaseController{
	
	static Connection conn = null;
	static DatabaseMetaData metaData = null;
	static Statement statment = null;
	static PreparedStatement prepstmt = null;
	
	/* 
	 * Create the database
	 */
	
	public static void createDatabase()
	{
		String url = "jdbc:sqlite:database.db";
		
		try{
			DatabaseController.conn = DriverManager.getConnection(url);
			if (conn != null)
			{
				DatabaseController.metaData = conn.getMetaData();
				System.out.println("La base de données a été crée !");
				DatabaseController.statment = conn.createStatement();
				
				
				String req = "CREATE TABLE IF NOT EXISTS USER (id integer NOT NULL PRIMARY KEY, identifiant text);";
				
				statment.execute(req);
				
				req = "CREATE TABLE IF NOT EXISTS MESSAGE(id integer PRIMARY KEY, date LONG, message text, idEmetteur integer, idRecepteur integer,FOREIGN KEY(idEmetteur) REFERENCES USER(id),FOREIGN KEY(idRecepteur) REFERENCES USER(id));";
				
				statment.execute(req);
				
				// Table avec l'utilisateur et son identifiant
				
				req = "CREATE TABLE IF NOT EXISTS CONNECTION (identifiant text NOT NULL PRIMARY KEY, password text);";
				
				statment.execute(req);
				
				
			}
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Add a user to the database
	 */
	
	public static void ajouterUser (UtilisateurActif user) {
		// Vérifier qu'il n'appartient pas à la bdd puis :
		
			try {
				DatabaseController.prepstmt = conn.prepareStatement("INSERT INTO USER (identifiant) VALUES (?)");
				DatabaseController.prepstmt.setString(1, user.GetIdentifiant());
				DatabaseController.prepstmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

	}
	
	
	/*
	 * Add a message to the database
	 */
	
	public static void addMessage(String mess,UtilisateurActif emetteur, UtilisateurActif dest)
	{
		Date d = new Date();
		Integer idEmetteur = getIdByIdentifiant(emetteur.GetIdentifiant());
		
		// Si l'emetteur n'est pas dans la BDD l'ajouter et récuperer son id
		if (idEmetteur == 0)
		{
			DatabaseController.ajouterUser(emetteur);
			idEmetteur = DatabaseController.getIdByIdentifiant(emetteur.GetIdentifiant());
		}
		
		int idDestinataire = getIdByIdentifiant(dest.GetIdentifiant());
		
		// Si le dest n'est pas dans la bdd, l'ajouter et récuperer son id
		if (idDestinataire ==  0)
		{
			DatabaseController.ajouterUser(dest);
			idDestinataire = DatabaseController.getIdByIdentifiant(dest.GetIdentifiant());
		}
				
		try {
			DatabaseController.prepstmt = conn.prepareStatement("INSERT INTO MESSAGE (date,message,idEmetteur,idRecepteur) VALUES (?,?,?,?)");
			DatabaseController.prepstmt.setLong(1, d.getTime());
			DatabaseController.prepstmt.setString(2, mess);
			DatabaseController.prepstmt.setInt(3, idEmetteur);
			DatabaseController.prepstmt.setInt(4, idDestinataire);
			prepstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public static ArrayList<Message> getMessageHistory (UtilisateurActif user1, UtilisateurActif user2){
		ArrayList<Message> listeMessages = new ArrayList<Message>();
		Integer idEmetteur = getIdByIdentifiant(user1.GetIdentifiant());
		Integer idDestinataire = getIdByIdentifiant(user2.GetIdentifiant());
		String sql = "SELECT id,date,message,idEmetteur,idRecepteur FROM MESSAGE WHERE idEmetteur="+idEmetteur+" and idRecepteur="+idDestinataire+";";
		
		try {
			DatabaseController.statment = DatabaseController.conn.createStatement();
			
			ResultSet rs = DatabaseController.statment.executeQuery(sql);
			
			while (rs.next())
			{
				listeMessages.add(new Message(rs.getString("message"),rs.getLong("date"),user1,user2));
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listeMessages ;
	}
	
	
	/*
	 * Returns a list of String with the date, pseudo and content of the messages exchanged
	 */
	
	public static ArrayList <String> getOrderedHistorytoString (ArrayList<Message> list){
		ArrayList<String> listString = new ArrayList<String> ();
		System.out.println("AFFICHAGE DE L'HISTORIQUE STOCKE");
		for (Message mess : list) {
			listString.add(new Date(mess.getDate()) + " " + mess.getEmetteur().GetPseudo() + " "+ mess.getMessage());
		}
		return listString ;
	}
	
	
	/*
	 * Returns an ordered list of string with all the messages exchanged between two users.
	 */
	
	public static ArrayList<String> getOrderedHistory(UtilisateurActif user1, UtilisateurActif user2)
	{
		ArrayList<Message> listOrdered = new ArrayList<Message>();
		listOrdered.addAll(DatabaseController.getMessageHistory(user1, user2));
		listOrdered.addAll(DatabaseController.getMessageHistory(user2,user1));
		Collections.sort(listOrdered, new MyMessagesComp());
		return getOrderedHistorytoString(listOrdered);
	}
	
	
	/*
	 * Returns the id of the user or 0 if it doesn't exist 
	 */
	
	public static Integer getIdByIdentifiant (String identifiant) {
		String sql = "SELECT id, identifiant FROM USER WHERE identifiant=\""+identifiant+"\";";
		Integer id = 0;
	
		try {
			DatabaseController.statment = DatabaseController.conn.createStatement();
			
			ResultSet rs = DatabaseController.statment.executeQuery(sql);
			
			while (rs.next())
			{
					id = rs.getInt("id");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;

	}
	
	private static void addUtilisateurToDatabase (String identifiant, String password) {
		try {
			DatabaseController.prepstmt = conn.prepareStatement("INSERT INTO CONNECTION (identifiant,password) VALUES (?,?)");
			DatabaseController.prepstmt.setString(1, identifiant);
			DatabaseController.prepstmt.setString(2, password);
			prepstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public static boolean verifyUtilisateur (String identifiant, String password) {
		boolean utilisateurValide = false ;
		try {
			ResultSet rs;
			rs = DatabaseController.statment.executeQuery("SELECT * FROM CONNECTION");
			if (!rs.next()) {
				addUtilisateurToDatabase(identifiant,password);
				utilisateurValide = true ;
			} else if (rs.getString("identifiant").equals(identifiant) && rs.getString("password").equals(password)) {
				utilisateurValide = true ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return utilisateurValide ;
	}


}
