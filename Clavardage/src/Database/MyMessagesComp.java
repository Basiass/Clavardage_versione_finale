package Database;

import java.util.Comparator;

public class MyMessagesComp implements Comparator <Message> {

	/*
	 * (non-Javadoc)
	 * returns 1 if o1 has been sent before o2
	 * returns -1 otherwise
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	
	@Override
	public int compare(Message o1, Message o2) {
		if (o1.getDate()>o2.getDate()) {
			return 1;
		}else {
			return -1;
		}
	}

}
