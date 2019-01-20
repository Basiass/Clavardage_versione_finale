package Views;

import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import Application.Conversation;
import Database.DatabaseController;
import Listener.ListenerOkButt;
import Listener.ListenerValiderButt;

public class ConnectionView {
	
	private JFrame frame;
	private static String labelId = "Veuillez entrer votre identifiant";
	final JLabel label = new JLabel(labelId);
	private static String labelmdp = "Veuillez entrer votre mot de passe";
	final JLabel label2 = new JLabel(labelmdp);
	private JTextArea identifiantTextArea;
	private JTextArea passwordTextArea;
	
	private Boolean identificationVerifiee = false ;
	private String identifiant ;
	
	public ConnectionView () {
		initialize();
	}
	
	public void initialize() {
		frame = new JFrame();
		frame.setTitle("Connection");
		frame.setVisible(true);
		frame.setBounds(200, 200,800,600);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(3, 0, 3, 0));
		
		frame.getContentPane().add(label);
		
		this.identifiantTextArea = new JTextArea();
		frame.getContentPane().add(identifiantTextArea);
		
		frame.getContentPane().add(label2);
		
		this.passwordTextArea = new JTextArea();
		frame.getContentPane().add(passwordTextArea);
		
		JButton btnValider = new JButton("Valider");
		frame.getContentPane().add(btnValider);
		
		ListenerValiderButt lvb = new ListenerValiderButt(this);
		btnValider.addActionListener(lvb);
		
	}

	public void VerifierIdentification() {
		String identifiant = identifiantTextArea.getText();
		String mdp = passwordTextArea.getText();
		if (identifiant.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Veuillez entrer un identifiant");
		} else if (mdp.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Veuillez entrer un mot de passe");
		}	else{
			
			if (DatabaseController.verifyUtilisateur(identifiant,mdp)) {
				frame.setVisible(false);
				this.identificationVerifiee = true ;
				this.identifiant = identifiant ;
			}else {
				JOptionPane.showMessageDialog(frame, "Identifiant ou mot de passe incorrect");
			};	
		}
		
		System.out.println(this.identificationVerifiee);
	}
	
	public boolean getIdentificationVerifiee () {
		return this.identificationVerifiee ;
	}

	public String getIdentifiant() {
		return this.identifiant;
	}
}
