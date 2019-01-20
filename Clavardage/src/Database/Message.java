package Database;

import Application.UtilisateurActif;

public class Message {
	private String mess ;
	private Long date ;
	private UtilisateurActif us1, us2 ;
	
	public Message (String mess, Long date, UtilisateurActif emetteur, UtilisateurActif dest) {
		this.mess = mess ;
		this.date = date ;
		this.us1 = emetteur ;
		this.us2 = dest ;
	}
	
	public Long getDate () {
		return this.date;
	}
	
	public String getMessage() {
		return this.mess ;
	}
	
	public UtilisateurActif getEmetteur() {
		return this.us1;
	}
}
