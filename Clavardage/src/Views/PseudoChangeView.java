package Views;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;

import Application.Conversation;
import Listener.*;

import java.awt.GridLayout;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class PseudoChangeView {
	
	private JFrame frame;
	private DefaultListModel<String> dlm;
	private JTextArea textArea;
	private Conversation conv;
	
	private mainWindowView mwv ;
	
	public PseudoChangeView (mainWindowView mwv) {
		this.mwv = mwv ;
		initialize();
	}
	
	public void initialize() {
		frame = new JFrame();
		frame.setTitle("Changement de pseudo");
		frame.setVisible(true);
		frame.setBounds(200, 200, 900, 600);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(3, 0, 3, 0));
		dlm = new DefaultListModel<String>();
		JList list = new JList(dlm);
		frame.getContentPane().add(list);
		
		this.textArea = new JTextArea();
		frame.getContentPane().add(textArea);
		
		dlm.addElement("Veuillez entrer votre nouveau pseudo");
		JButton btnOk = new JButton("Ok");
		frame.getContentPane().add(btnOk);
		
		ListenerOkButt lob = new ListenerOkButt(this);
		btnOk.addActionListener(lob);
	}
	
	public void verifierPseudo() {
		// fonction verifier changement de pseudo
		String newPseudo = textArea.getText();

		if (!mwv.getUtilisateur().VerifierPseudoDispo(newPseudo)) {
			JOptionPane.showMessageDialog(frame, "Le pseudo que vous avez entré est déjà pris, veuillez en entrer un autre");
		} else if (newPseudo.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Veuillez entrer un pseudo");
		}	else{
			frame.setVisible(false);
			changerPseudo(newPseudo);	
		}
		
	}
	
	public void changerPseudo(String newpseudo) {
		this.mwv.getUtilisateur().ChangementPseudo(mwv.getUtilisateurActif(),newpseudo);
	}
}
