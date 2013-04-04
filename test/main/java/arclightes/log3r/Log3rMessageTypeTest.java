package main.java.arclightes.log3r;

import org.junit.Test;

public class Log3rMessageTypeTest {

	public Log3rMessageTypeTest() {

	}

	@Test
	public void testGetLogMessage() {
		final LogMessageType messageType = Log3rMessageType.ARRAY;
		final SequencedLogMessage newMessage = Log3r.getLog3r().getSequencedLogMessage(messageType);
		final CharArrayMessage appendableMsg = (CharArrayMessage) newMessage.m();
		appendableMsg.append('c').append('i').append('j');
		System.out.println(newMessage.m().toString());
		Log3r.getLog3r().log(newMessage);
	}
}
