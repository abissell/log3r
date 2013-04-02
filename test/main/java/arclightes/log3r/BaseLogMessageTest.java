package main.java.arclightes.log3r;

import junit.framework.Assert;
import org.junit.Test;

public class BaseLogMessageTest {
	private static final int TEST_LENGTH = 400;
	private static final char[] RANDOM_CHARS = TestUtils.getRandomCharArray(TEST_LENGTH);

	public BaseLogMessageTest() {

	}

	@Test
	public void test() {
		final BaseLogMessage testMessage = new CharArrayLog3rMessage();

		((CharArrayLogMessage) testMessage).append(RANDOM_CHARS);

		final char[] testMessageArray = testMessage.array();
		for (int i = 0; i < TEST_LENGTH; i++) {
			Assert.assertEquals(testMessageArray[i], RANDOM_CHARS[i]);
		}
		Assert.assertEquals(testMessage.msgLength(), TEST_LENGTH);

		testMessage.reset();
		Assert.assertEquals(testMessage.msgLength(), 0);
	}
}
