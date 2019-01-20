package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Views.PseudoChangeView;

public class ListenerOkButt implements ActionListener {

	PseudoChangeView pcv;
	
	public ListenerOkButt(PseudoChangeView c)
	{
		pcv = c;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		pcv.verifierPseudo();
	}

}
