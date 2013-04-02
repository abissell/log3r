package main.java.arclightes.log3r;

import org.junit.Assert;
import org.junit.Test;

import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;

public class BasicCharBlockTest {
	private static final CharBlock TEST_BLOCK = BasicCharBlock.DASH;

	public BasicCharBlockTest() {

	}

	@Test
	public void test() {
		Assert.assertArrayEquals(TEST_BLOCK.array(), BasicCharBlock.DASH.array());
		final CharBuffer buffer = TEST_BLOCK.buffer();
		try {
			buffer.append('x');
			Assert.assertEquals(true, false);
		} catch (ReadOnlyBufferException e) {
			System.out.println("Attempt to modify buffer threw exception as desired");
		}
	}
}
