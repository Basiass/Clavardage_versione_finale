package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Views.ConnectionView;

public class ListenerValiderButt implements ActionListener {

	ConnectionView cnctView ;
	

	public ListenerValiderButt(ConnectionView cnctView) {
		this.cnctView = cnctView ;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.cnctView.VerifierIdentification();
		
	}
}
