package main.java.arclightes.log3r;

import org.junit.Test;

public class Log3rMessageTypeTest {

	public Log3rMessageTypeTest() {

	}

	@Test
	public void testGetLogMessage() {
		final LogMessageType messageType = Log3rMessageType.ARRAY;
		final LogMessage newMessage = messageType.getNextMessage();
	}
}
