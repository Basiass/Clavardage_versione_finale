package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Views.ConversationView;
import Views.mainWindowView;

public class ListenerDecoButton implements ActionListener{

		mainWindowView mwv;
		
		public ListenerDecoButton(mainWindowView c)
		{
			mwv = c;
		}
		
		@Override
		
		public void actionPerformed(ActionEvent e) {
			mwv.seDeconnecter();
		}
	

}
