package servlet_package;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lib.Serveur;
import lib.UtilisateurActif;


//import src.Application.UtilisateurActif;

/**
 * Servlet implementation class Servlet
 */
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static ArrayList<UtilisateurActif> listeUsersActifs = new ArrayList<UtilisateurActif>();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	//public Servlet(Serveur serveur) {
        //this.serveur = serveur;
        // TODO Auto-generated constructor stub
    //}
	 
	public Servlet () {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	/*public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		listeUsersActifs = (ArrayList<UtilisateurActif>) getServletContext().getAttribute("liste");
		//if (listeUsersActifs == null) throw new UnavailableException("Couldn’t get database.");
	}*/

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		Boolean dejaCo = false;
		Boolean deconnection = false;
		Boolean maj = false;

		//String pseudo = request.getHeader("pseudo");
		String pseudo = request.getParameter("pseudo");
		System.out.println("Pseudo d'où provient la requete"+ pseudo);
		if (pseudo != null) {
			
			String identifiant = request.getParameter("identifiant");
			InetAddress adrip = InetAddress.getByName(request.getRemoteAddr());
			String dejaCoString = request.getParameter("dejaCo");  // 0 si déja co, 1 si pas deja co
			String majString = request.getParameter("maj"); // 1 si maj
			if (dejaCoString.equals("0")) {
				dejaCo = true;}
			String deconnectionString = request.getParameter("deconnection");
			if (deconnectionString.equals("0")) { // 1 si pas deconnection
				deconnection = true;}
			if (majString.equals("1")) {
				maj = true;}


			UtilisateurActif expediteur = new UtilisateurActif(pseudo, identifiant, adrip, dejaCo, deconnection);
			response.setContentType("text.html");
			PrintWriter out = response.getWriter();
			//out.println("<h1> Bienvenue </h1>" );
			/*out.println("<html>") ;
			out.println("<head>") ;
			out.println("<title>Bonjour le monde !</title>") ;
			out.println("</head>") ;
			out.println("<body>") ;
			out.println("<h1>Bonjour le monde !</h1>") ;
			out.println("</body>") ;
			out.println("</html>") ;*/

			// nouvelle connection ou demande de mise à jour liste
			if (!dejaCo || maj) {
				Servlet.listeUsersActifs.add(expediteur);
				System.out.println("laaaaaaaaaaaaaaaaaaaa " + listeUsersActifs.get(0).GetPseudo());
				// repondre la liste des utilisateurs actifs
				//for (UtilisateurActif us : this.serveur.getListeUsersActifs()) {
				for (UtilisateurActif us : listeUsersActifs) {
					//System.out.print("," + us.GetPseudo() + "," + us.GetIdentifiant() + "," + us.getInetAddress() + ";");
					out.println("," + us.GetPseudo() + "," + us.GetIdentifiant() + "," + us.getInetAddress() + ";");
				}
				if (listeUsersActifs == null)
					out.println("rien");
				out.println("§");
				AjouterUserActif(expediteur);
				
			}

			// deconnection
			if(deconnection) {
				SupprimerUserActif(expediteur);
				out.println("A bientot");

			}

			// changement de pseudo
			if(dejaCo && !deconnection) { 
				majPseudo(expediteur);
				out.println("Bien recu !");
			}
			out.flush(); out.close();
		}


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	// CONNECTION
	// Ajouter à la liste
	public void AjouterUserActif(UtilisateurActif nouveau){
		Servlet.listeUsersActifs.add(nouveau);
	}
	
	
	// DECONNECTION
	// Enlever de la la liste
	public void SupprimerUserActif(UtilisateurActif userToDelete){
		for (UtilisateurActif userActif : listeUsersActifs)
		{
			if (userActif.GetIdentifiant().equals(userToDelete.GetIdentifiant())) { 
				listeUsersActifs.remove(userActif);
				break;
			}
		}
	}
	
	// Envoyer majpseudo
	// Mise à jour pseudo
	public void majPseudo (UtilisateurActif expediteur) {
		for (UtilisateurActif useractif : listeUsersActifs ) {
			if (expediteur.GetIdentifiant().equals(useractif.GetIdentifiant())) {
				useractif.setPseudo(expediteur.GetPseudo());
				break;
			}
		}
	}
}
