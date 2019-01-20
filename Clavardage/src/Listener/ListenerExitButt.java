package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Views.ConversationView;

public class ListenerExitButt implements ActionListener {

	ConversationView cv;
	
	public ListenerExitButt(ConversationView c)
	{
		cv = c;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		cv.fermerConversation() ;
	}
}
