package Views;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;

import Application.Conversation;
import Application.Utilisateur;
import Application.UtilisateurActif;
import Database.DatabaseController;
import Listener.*;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class ConversationView {

	private JFrame frame;
	private DefaultListModel<String> dlm;
	private JTextArea textArea;
	private Conversation conv;

	private JButton btnSend ;
	/**
	 * Create the application.
	 */
	public ConversationView(Conversation c) {
		this.conv = c;
		initialize();
	}

	public Conversation getConversation() {
		return this.conv;
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//frame.setTitle(conv.getDest().GetPseudo());
		frame.setVisible(true);
		frame.setBounds(200, 200, 900, 600);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(3, 0, 3, 0));
		dlm = new DefaultListModel<String>();
		JList list = new JList(dlm);
		frame.getContentPane().add(list);
		
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridheight = 6;
		gbc_list.gridwidth = 10;
		gbc_list.insets = new Insets(0, 0, 5, 0);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollPane, gbc_list);
		
		this.textArea = new JTextArea();
		frame.getContentPane().add(textArea);
		
		btnSend = new JButton("Envoyer");
		frame.getContentPane().add(btnSend);
		
		ListenerSendButt lsb = new ListenerSendButt(this);
		btnSend.addActionListener(lsb);
		
		JButton btnExit = new JButton("Exit");
		frame.getContentPane().add(btnExit);
		
		ListenerExitButt leb = new ListenerExitButt(this);
		btnExit.addActionListener(leb);
	
	}
	
	public JTextArea getJTextArea()
	{
		return this.textArea;
	}

	public void sendMessage(String contenu) {
		Date d = new Date();
		dlm.addElement(d+" " + conv.getUtilisateur().getPseudo()+ " " + contenu);	
		this.conv.sendMessage(contenu);
		DatabaseController.addMessage(contenu, new UtilisateurActif(conv.getUtilisateur().getPseudo(), conv.getUtilisateur().getIdentifiant(),conv.getUtilisateur().getAdrIP(),true,false), conv.getDest());
	}
	
	public void receive(String contenu) {
		Date d = new Date();
		dlm.addElement(d+ " "+ conv.getDest().GetPseudo()+" "+contenu);
		DatabaseController.addMessage(contenu, conv.getDest(), new UtilisateurActif(conv.getUtilisateur().getPseudo(), conv.getUtilisateur().getIdentifiant(),conv.getUtilisateur().getAdrIP(),true,false));
	}
	
	public void fermerConversation() {
		if (frame.isVisible()) {this.conv.terminerConversation();};
		frame.setVisible(false);
	}
	
	public void desactiverAffichageFrame() {
		frame.setVisible(false);
	}
	
	public void desactiverSendBtn () {
		dlm.addElement("L'autre utilisateur a fermé la fenêtre de chat.");
		dlm.addElement("Vous ne pouvez plus lui envoyer de messages.");
		dlm.addElement("Il faut commencer une nouvelle conversation ");
		btnSend.setEnabled(false);
	}

	public void afficherAnciensMessages() {
		Utilisateur user = conv.getUtilisateur();
		for (String mess : DatabaseController.getOrderedHistory(new UtilisateurActif(user.getPseudo(), user.getIdentifiant(),user.getAdrIP(),true,false),conv.getDest())) {
			dlm.addElement(mess);
		}
	}

	public void afficherChangementPseudo() {
		dlm.addElement("Votre interlocuteur vient de changer de pseudo");
	}

	
}
