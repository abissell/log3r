import junit.framework.Assert;
import org.junit.Test;

public class TestLog3rUtilsCorrectness {
	@Test
	public void testCharIntConversion() {
		char c = '1';
		int converted = Log3rUtils.getInt(c);
		Assert.assertEquals(converted, 1);
		c = Log3rUtils.getChar(converted);
		Assert.assertEquals(c, '1');

		c = '2';
		converted = Log3rUtils.getInt(c);
		Assert.assertEquals(converted, 2);
		c = Log3rUtils.getChar(converted);
		Assert.assertEquals(c, '2');

		c = '3';
		converted = Log3rUtils.getInt(c);
		Assert.assertEquals(converted, 3);
		c = Log3rUtils.getChar(converted);
		Assert.assertEquals(c, '3');

		c = '4';
		converted = Log3rUtils.getInt(c);
		Assert.assertEquals(converted, 4);
		c = Log3rUtils.getChar(converted);
		Assert.assertEquals(c, '4');

		c = '5';
		converted = Log3rUtils.getInt(c);
		Assert.assertEquals(converted, 5);
		c = Log3rUtils.getChar(converted);
		Assert.assertEquals(c, '5');

		c = '6';
		converted = Log3rUtils.getInt(c);
		Assert.assertEquals(converted, 6);
		c = Log3rUtils.getChar(converted);
		Assert.assertEquals(c, '6');

		c = '7';
		converted = Log3rUtils.getInt(c);
		Assert.assertEquals(converted, 7);
		c = Log3rUtils.getChar(converted);
		Assert.assertEquals(c, '7');

		c = '8';
		converted = Log3rUtils.getInt(c);
		Assert.assertEquals(converted, 8);
		c = Log3rUtils.getChar(converted);
		Assert.assertEquals(c, '8');

		c = '9';
		converted = Log3rUtils.getInt(c);
		Assert.assertEquals(converted, 9);
		c = Log3rUtils.getChar(converted);
		Assert.assertEquals(c, '9');

		c = '0';
		converted = Log3rUtils.getInt(c);
		Assert.assertEquals(converted, 0);
		c = Log3rUtils.getChar(converted);
		Assert.assertEquals(c, '0');
	}

	@Test
	public void printDoubleStrings() {
		for (int i = 0; i <= 32; i++) {
			System.out.println(i + "=" + Integer.toBinaryString(i) + ", ");
		}
	}


	// @Test
	public void demonstratePseudoRandom() {
		for (int i = 0; i < 20; i++) {
			System.out.println(TestUtils.randomInt(1000));
		}
	}
}
