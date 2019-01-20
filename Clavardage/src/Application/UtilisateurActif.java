package Application;
import java.io.Serializable;
import java.net.InetAddress;


public class UtilisateurActif implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Identifiant; 
	private String Pseudo;
	private InetAddress adrIP;
	private Boolean enCoursDeConversation ;
	private boolean dejaCo ;
	private boolean deconnection ;
	
	public UtilisateurActif(String pseudo, String Identifiant, InetAddress adrip, boolean co, boolean deco) {

		this.Pseudo = pseudo;
		this.Identifiant = Identifiant;
		this.adrIP = adrip;
		this.enCoursDeConversation = false;
		this.dejaCo=co;
		this.deconnection = deco ;
	}
	
	public String GetIdentifiant() {
		return Identifiant;
	}
	
	public String GetPseudo() {
		return Pseudo;
	}
	
	public void setPseudo(String Nouveau) {
		this.Pseudo = Nouveau; 
	}
	
	public InetAddress getInetAddress () {
		return this.adrIP;
	}
	
	public void setEnCoursDeConversation (boolean val) {
		this.enCoursDeConversation = val ;
	}
	
	public boolean getEnCoursDeConversation () {
		return this.enCoursDeConversation;
	}
	
	public boolean getDejaCo () {
		return this.dejaCo;
	}
	
	public boolean getDeco () {
		return this.deconnection;
	}
	
	public void setDejaCo (boolean val) {
		this.dejaCo = val ;
	}
}

