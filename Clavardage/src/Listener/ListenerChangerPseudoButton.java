package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Application.UtilisateurActif;
import Views.mainWindowView;

public class ListenerChangerPseudoButton implements ActionListener {

	private mainWindowView mwv ;
	private UtilisateurActif userDest ;
	
	public ListenerChangerPseudoButton (mainWindowView mwv,UtilisateurActif userDest) {
		this.mwv = mwv;
		this.userDest = userDest ;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		mwv.changeMonPseudo(userDest);
	}
}
