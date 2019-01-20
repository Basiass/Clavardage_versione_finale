package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import Views.ConversationView;

public class ListenerSendButt implements ActionListener{

	ConversationView cv;
	
	public ListenerSendButt(ConversationView c)
	{
		cv = c;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String contenu = cv.getJTextArea().getText();
		cv.sendMessage(contenu);
	}

}
