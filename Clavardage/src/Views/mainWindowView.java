package Views;

import java.awt.GridLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import Application.Conversation;
import Application.Utilisateur;
import Application.UtilisateurActif;
import Database.DatabaseController;
import Listener.ListenerChangerPseudoButton;
import Listener.ListenerDecoButton;
import Listener.ListenerExitButt;
import Listener.ListenerSendButt;
import Listener.ListenerUserCoButton;

public class mainWindowView {

	private Utilisateur user;
	private UtilisateurActif me;
	private JFrame frame;

	private ArrayList<Conversation> listConversation = new ArrayList<Conversation> ();
	private ArrayList<JButton> listButtonUserActif = new ArrayList<JButton>();
	
	public mainWindowView (Utilisateur user,UtilisateurActif moi) {
		this.user = user;
		this.me = moi ;
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Fenêtre principale");
		frame.setVisible(true);
		frame.setBounds(200, 200, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(3, 0, 3, 0));
		//dlm = new DefaultListModel<String>();
		//JList list = new JList(dlm);
		//frame.getContentPane().add(list);
		
		JButton btnDeconnexion = new JButton("Deconnection");
		frame.getContentPane().add(btnDeconnexion);
		
		ListenerDecoButton ldb = new ListenerDecoButton(this);
		btnDeconnexion.addActionListener(ldb);
		
		JButton btnChgtPseudo = new JButton("Changer de pseudo");
		frame.getContentPane().add(btnChgtPseudo);
		
		ListenerChangerPseudoButton lcpb = new ListenerChangerPseudoButton(this, this.me);
		btnChgtPseudo.addActionListener(lcpb);
		
	}
	
	/*
	 * Add user to the main window
	 */
	
	public void addConnectedUser(UtilisateurActif userCo) {
		JButton btnUser = new JButton(userCo.GetPseudo());
		frame.getContentPane().add(btnUser);
		
		ListenerUserCoButton lucb = new ListenerUserCoButton(this,userCo);
		btnUser.addActionListener(lucb);
		
		listButtonUserActif.add(btnUser);
	}
	
	/*
	 *  Delete user from main Window
	 */
	
	public void deleteUser(UtilisateurActif userToDel) {
		for (JButton butt : this.listButtonUserActif) {
			if (butt.getText().equals(userToDel.GetPseudo())) {
				frame.getContentPane().remove(butt);
				break;
			}
		}
	}
	
	
	public void changeMonPseudo (UtilisateurActif userdest) {
		PseudoChangeView pcv = new PseudoChangeView(this);
	}
	
	public void majPseudoDest (String newPseudo, String oldPseudo) {
		for (JButton butt : this.listButtonUserActif) {
			if (butt.getText().equals(oldPseudo)) {
				butt.setText(newPseudo);
				break;
			}
		}
	}
	
	public void commencerConversation(UtilisateurActif userDest) {
		Conversation Conv = new Conversation (user); 
		Conv.initiate(userDest,this);
		this.listConversation.add(Conv);
		
	}
	
	
	
	public UtilisateurActif getUtilisateurActif () {
		return this.me ;
	}
	
	public Utilisateur getUtilisateur () {
		return this.user;
	}
	
	
	/*
	 * 	Gestion du cas où le programme côté utilisateur beugue
	 */
	public void afficherFenetreUtilisateurDeconnecte(Conversation conv) {
		JOptionPane.showMessageDialog(frame, "L'utilisateur n'est plus connecté");
		this.user.SupprimerUserActif(conv.getDest());
		deleteUser(conv.getDest());
		
		for (Conversation conversation : this.listConversation) {
			if (conversation.equals(conv)) {
				this.listConversation.remove(conv);
			}
		}
	}
	
	public void afficherChangementPseudoConversation(UtilisateurActif userdest) {
		for (Conversation conv : this.listConversation) {
			if (conv.getDest() == userdest) {
				conv.getConvView().afficherChangementPseudo();
			}
		}
	}
	
	/*
	 * 	Se Deconnecter
	 */
	
	public void seDeconnecter() {
		
		this.user.SeDeconnecter(new UtilisateurActif(this.user.getPseudo(),this.user.getIdentifiant(),this.user.getAdrIP(),true,true));
		for (Conversation conv : listConversation ) {
			conv.seDeconnecter();
		}
		frame.setVisible(false);
	}
}
