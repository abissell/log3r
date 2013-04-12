package main.java.arclightes.log3r;

import org.junit.Test;

public class JavaChronicleAppenderTest {

	public JavaChronicleAppenderTest() {

	}

	@Test
	public void testAppendNumerals() {
		char[] doubleChar = new char[9];
		JavaChronicleAppender.appendDouble(-123.4567, doubleChar, 0);
		System.out.println(doubleChar);

		printAppendLong(1234567L);
		printAppendLong(-1234567L);
		printAppendLong(0L);

		printAppendInt(1234567);
		printAppendInt(-1234567);
		printAppendInt(0);

		printZeroPaddedNonNegativeInt(23, 4);
		printZeroPaddedNonNegativeInt(100, 4);
		printZeroPaddedNonNegativeInt(0, 5);
		printZeroPaddedNonNegativeInt(8, 3);
		printZeroPaddedNonNegativeInt(1, 3);
	}

	private void printAppendInt(final int i) {
		char[] intChar = new char[50];
		JavaChronicleAppender.appendInt(i, intChar, 0);
		System.out.println(intChar);
	}

	private void printAppendLong(final long el) {
		char[] longChar = new char[50];
		JavaChronicleAppender.appendLong(el, longChar, 0);
		System.out.println(longChar);
	}

	private void printZeroPaddedNonNegativeInt(final int i, final int minLength) {
		char[] zeroPaddedLongChar = new char[50];
		JavaChronicleAppender.appendZeroPaddedNonNegativeInt(i, minLength, zeroPaddedLongChar, 0);
		System.out.println(zeroPaddedLongChar);
	}
}
