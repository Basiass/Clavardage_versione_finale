package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Application.UtilisateurActif;
import Views.mainWindowView;

public class ListenerUserCoButton implements ActionListener {

	private mainWindowView mwv ;
	private UtilisateurActif userDest ;
	
	public ListenerUserCoButton (mainWindowView mwv,UtilisateurActif userDest) {
		this.mwv = mwv ;
		this.userDest = userDest;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		mwv.commencerConversation(userDest);
	}
}
