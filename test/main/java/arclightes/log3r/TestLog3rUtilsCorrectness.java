package main.java.arclightes.log3r;

import junit.framework.Assert;
import org.junit.Test;

public class TestLog3rUtilsCorrectness {
	@Test
	public void testCharIntConversion() {
		char c = '1';
		int converted = (c - '0');
		Assert.assertEquals(converted, 1);
		c = (char) (c + '0');
		Assert.assertEquals(c, '1');

		c = '2';
		converted = (c - '0');
		Assert.assertEquals(converted, 2);
		c = (char) (c + '0');
		Assert.assertEquals(c, '2');

		c = '3';
		converted = (c - '0');
		Assert.assertEquals(converted, 3);
		c = (char) (c + '0');
		Assert.assertEquals(c, '3');

		c = '4';
		converted = (c - '0');
		Assert.assertEquals(converted, 4);
		c = (char) (c + '0');
		Assert.assertEquals(c, '4');

		c = '5';
		converted = (c - '0');
		Assert.assertEquals(converted, 5);
		c = (char) (c + '0');
		Assert.assertEquals(c, '5');

		c = '6';
		converted = (c - '0');
		Assert.assertEquals(converted, 6);
		c = (char) (c + '0');
		Assert.assertEquals(c, '6');

		c = '7';
		converted = (c - '0');
		Assert.assertEquals(converted, 7);
		c = (char) (c + '0');
		Assert.assertEquals(c, '7');

		c = '8';
		converted = (c - '0');
		Assert.assertEquals(converted, 8);
		c = (char) (c + '0');
		Assert.assertEquals(c, '8');

		c = '9';
		converted = (c - '0');
		Assert.assertEquals(converted, 9);
		c = (char) (c + '0');
		Assert.assertEquals(c, '9');

		c = '0';
		converted = (c - '0');
		Assert.assertEquals(converted, 0);
		c = (char) (c + '0');
		Assert.assertEquals(c, '0');
	}

	@Test
	public void testCharLongConversion() {
		char c = '1';
		long converted = c - '0';
		Assert.assertEquals(converted, 1L);
		c = (char) (c + '0');
		Assert.assertEquals(c, '1');
	}

	@Test
	public void printMaxValues() {
		System.out.println(Long.MAX_VALUE);
		final double maxDouble = Double.MAX_VALUE;
		System.out.println(Double.MAX_VALUE);
		System.out.println(Math.getExponent(maxDouble));
	}

	@Test
	public void printDoubleStrings() {
		for (int i = 0; i <= 32; i++) {
			System.out.println(i + "=" + Integer.toBinaryString(i) + ", ");
		}
	}

	@Test
	public void testInfinityRelationships() {
		Assert.assertEquals(Double.POSITIVE_INFINITY > 0, true);
		Assert.assertEquals(Double.NEGATIVE_INFINITY < 0, true);
	}

	// @Test
	public void demonstratePseudoRandom() {
		for (int i = 0; i < 20; i++) {
			System.out.println(TestUtils.randomInt(1000));
		}
	}
}
