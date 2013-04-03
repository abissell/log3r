package main.java.arclightes.log3r;

import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableDateTime;
import org.junit.Assert;
import org.junit.Test;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CharArrayLog3rMessageTest {
	private static final int TEST_LENGTH = 400;
	private static final char[] RANDOM_CHARS = TestUtils.getRandomCharArray(TEST_LENGTH);
	private static final Object[] RANDOM_SUBARRAYS;
	static {
		Object[] subArrayHolder = new Object[TEST_LENGTH];
		int subArraysCreated = 0, randomArrayIdx = 0;
		while (randomArrayIdx < TEST_LENGTH) {
			int subArrayLength = TestUtils.randomInt(15);
			if (randomArrayIdx + subArrayLength > TEST_LENGTH)
				subArrayLength = TEST_LENGTH - randomArrayIdx;

			final char[] newArray = new char[subArrayLength];
			System.arraycopy(RANDOM_CHARS, randomArrayIdx, newArray, 0, subArrayLength);
			subArrayHolder[subArraysCreated++] = newArray;
			randomArrayIdx += subArrayLength;
		}

		Object[] subArrays = new Object[subArraysCreated];
		for (int i = 0; i < subArrays.length; i++) {
			subArrays[i] = subArrayHolder[i];
		}

		RANDOM_SUBARRAYS = subArrays;
	}

	public CharArrayLog3rMessageTest() {

	}

	@Test
	public void testAppendLogMessage() {
   		final char[] masterArray = new char[5000];
		int masterLength = 0;

		int randomSubArrayIdx = 0;
		final CharArrayLogMessage baseMessage = getRandomLogMessage(5, randomSubArrayIdx);
		randomSubArrayIdx += 5;
		System.arraycopy(baseMessage.array(), 0, masterArray, masterLength, baseMessage.msgLength());
		masterLength += baseMessage.msgLength();

		final CharArrayLogMessage appendedMessage = getRandomLogMessage(5, randomSubArrayIdx);
		System.arraycopy(appendedMessage.array(), 0, masterArray, masterLength, appendedMessage.msgLength());
		masterLength += appendedMessage.msgLength();

		baseMessage.append(appendedMessage);

		System.out.println(baseMessage.toString());
		System.out.println(String.valueOf(Arrays.copyOfRange(masterArray, 0, masterLength)));
		Assert.assertArrayEquals(getMessageSubArray(baseMessage), Arrays.copyOfRange(masterArray, 0, masterLength));
	}

	@Test
	public void testAppendCharBlock() {
		final char[] base = {'a', 'b', 'c'};
		final CharArrayLogMessage message = new CharArrayLog3rMessage();
		message.append(base);
		message.append(BasicCharBlock.TIMES);

		final char[] target = {'a', 'b', 'c', ' ', '*', ' '};
		Assert.assertArrayEquals(target, getMessageSubArray(message));
	}

	@Test
	public void testAppendChar() {
		final char[] base = {'a', 'b'};
		final CharArrayLogMessage message = new CharArrayLog3rMessage();
		message.append('c');

		final char[] target = {'c'};
		Assert.assertArrayEquals(getMessageSubArray(message), target);
	}

	@Test
	public void testAppendCharArrays() {
		final char[] masterArray = new char[5000];
		int masterLength = 0;

		int randomSubArrayIdx = 0;
		final CharArrayLogMessage baseMessage = getRandomLogMessage(5, randomSubArrayIdx);
		randomSubArrayIdx += 5;
		masterLength += copyMessageRangeToMasterArray(baseMessage, 0, masterArray, masterLength);

		final char[] randomSubArray = (char[]) RANDOM_SUBARRAYS[randomSubArrayIdx++];
		baseMessage.append(randomSubArray);
		System.arraycopy(randomSubArray, 0, masterArray, masterLength, randomSubArray.length);
		masterLength += randomSubArray.length;

		Assert.assertArrayEquals(getMessageSubArray(baseMessage), Arrays.copyOfRange(masterArray, 0, masterLength));

		char[] randomSubArray2;
		do {
			randomSubArray2 = (char[]) RANDOM_SUBARRAYS[randomSubArrayIdx++];
		} while (randomSubArray2.length < 10);

		final int randomCopyLength = randomSubArray2.length - 3;
		baseMessage.append(randomSubArray2, 2, randomCopyLength);
		System.arraycopy(randomSubArray2, 2, masterArray, masterLength, randomCopyLength);
		masterLength += randomCopyLength;

		System.out.println(baseMessage.toString());
		System.out.println(String.valueOf(Arrays.copyOfRange(masterArray, 0, masterLength)));
		Assert.assertArrayEquals(getMessageSubArray(baseMessage), Arrays.copyOfRange(masterArray, 0, masterLength));
	}

	@Test
	public void testAppendCharBuffer() {
		final CharArrayLogMessage message = new CharArrayLog3rMessage();
		message.append('a').append('b').append('c');
		final CharBuffer buffer = BasicCharBlock.TIMES.buffer();
		System.out.println(buffer.toString());
		System.out.println("buffer length=" + buffer.length());
		message.append(buffer);

		System.out.println(message);
		System.out.println(String.valueOf(message.array()));

		final char[] target = {'a', 'b', 'c', ' ', '*', ' '};
		Assert.assertArrayEquals(getMessageSubArray(message), target);
	}

	@Test
	public void testAppendIntegers() {
		final char[] masterArray = new char[5000];
		int masterLength = 0;

		final CharArrayLogMessage message = getRandomLogMessage(5, 0);
		masterLength += copyMessageRangeToMasterArray(message, 0, masterArray, masterLength);

		masterLength += appendToLogMessageAndMasterArray(0, message, masterArray, masterLength);
		masterLength += appendToLogMessageAndMasterArray(-249710900, message, masterArray, masterLength);
		masterLength += appendToLogMessageAndMasterArray(1400907789, message, masterArray, masterLength);

		masterLength += appendToLogMessageAndMasterArray(0L, message, masterArray, masterLength);
		masterLength += appendToLogMessageAndMasterArray(-2497109918501286350L, message, masterArray, masterLength);
		masterLength += appendToLogMessageAndMasterArray(1409077890000342343L, message, masterArray, masterLength);

		System.out.println(message);
		System.out.println(Arrays.copyOfRange(masterArray, 0, masterLength));
		Assert.assertArrayEquals(getMessageSubArray(message), Arrays.copyOfRange(masterArray, 0, masterLength));
	}

	@Test
	public void testAppendDoubles() {
		final char[] masterArray = new char[5000];
		int masterLength = 0;

		final CharArrayLogMessage message = getRandomLogMessage(5, 0);
		masterLength += copyMessageRangeToMasterArray(message, 0, masterArray, masterLength);

		masterLength += appendToLogMessageAndMasterArray(0.12345, -1, message, masterArray, masterLength);

		masterLength += appendToLogMessageAndMasterArray(0.0d, 1, message, masterArray, masterLength);
		masterLength += appendToLogMessageAndMasterArray(Double.NaN, 10, message, masterArray, masterLength);
		masterLength += appendToLogMessageAndMasterArray(Double.POSITIVE_INFINITY, 10, message, masterArray, masterLength);
		masterLength += appendToLogMessageAndMasterArray(Double.NEGATIVE_INFINITY, 10, message, masterArray, masterLength);

		masterLength += appendToLogMessageAndMasterArray(-2497.305, 3, message, masterArray, masterLength);
		masterLength += appendToLogMessageAndMasterArray(14.21009, 5, message, masterArray, masterLength);
		masterLength += appendToLogMessageAndMasterArray(-0.32104, 5, message, masterArray, masterLength);
		masterLength += appendToLogMessageAndMasterArray(0.69214, 5, message, masterArray, masterLength);

		System.out.println(message);
		System.out.println(Arrays.copyOfRange(masterArray, 0, masterLength));
		Assert.assertArrayEquals(getMessageSubArray(message), Arrays.copyOfRange(masterArray, 0, masterLength));
	}

	@Test
	public void testAppendMillisecondTimestampsBothLibraries() {
		testAppendMillisecondTimestamps(DateLibrary.JDK);
		testAppendMillisecondTimestamps(DateLibrary.JODA);
	}

	private void testAppendMillisecondTimestamps(final DateLibrary dateLibrary) {
		final Calendar c = new GregorianCalendar();
		final long startTime = System.currentTimeMillis();
		long curTime = System.currentTimeMillis();
		int tests = 0;
		while (curTime - startTime <= 5000) {
			testAppendMillisecondTimestamp(dateLibrary, c, new MutableDateTime());
			curTime = System.currentTimeMillis();
			++tests;
		}

		System.out.println(tests + " millsecond timestamp append tests performed");
	}

	private static void testAppendMillisecondTimestamp(final DateLibrary library, final Calendar c, final ReadWritableDateTime dateTime) {
		final long currentTimeMs = System.currentTimeMillis();
		final CharArrayLogMessage message;
		c.setTimeInMillis(currentTimeMs);
		if (library == DateLibrary.JDK) {
			message = new CharArrayLog3rMessage();
		} else {
			dateTime.setMillis(currentTimeMs);
			message = new JodaCharArrayLog3rMessage();
		}

		message.appendMillisecondTimestamp(currentTimeMs);

		Assert.assertArrayEquals(Arrays.copyOfRange(message.array(), 0, 4), Integer.toString(c.get(Calendar.YEAR)).toCharArray());

		final char[] monthTarget = new char[3];
		final int monthTargetInt = c.get(Calendar.MONTH) + 1;
		monthTarget[0] = '-';
		monthTarget[1] = monthTargetInt >= 10 ? '1' : '0';
		monthTarget[2] = Integer.toString(monthTargetInt % 10).charAt(0);
		Assert.assertArrayEquals(Arrays.copyOfRange(message.array(), 4, 7), monthTarget);

		final char[] dayOfMonthTarget = new char[3];
		final int dayOfMonthTargetInt = c.get(Calendar.DAY_OF_MONTH);
		dayOfMonthTarget[0] = '-';
		dayOfMonthTarget[1] = dayOfMonthTargetInt >= 10 ? Integer.toString(dayOfMonthTargetInt).charAt(0) : '0';
		dayOfMonthTarget[2] = Integer.toString(dayOfMonthTargetInt % 10).charAt(0);
		Assert.assertArrayEquals(Arrays.copyOfRange(message.array(), 7, 10), dayOfMonthTarget);

		final char[] hourTarget = new char[3];
		final int hourTargetInt = c.get(Calendar.HOUR_OF_DAY);
		hourTarget[0] = ' ';
		hourTarget[1] = hourTargetInt >= 10 ? Integer.toString(hourTargetInt).charAt(0) : '0';
		hourTarget[2] = Integer.toString(hourTargetInt % 10).charAt(0);
		Assert.assertArrayEquals(Arrays.copyOfRange(message.array(), 10, 13), hourTarget);

		final char[] minuteTarget = new char[3];
		final int minuteTargetInt = c.get(Calendar.MINUTE);
		minuteTarget[0] = ':';
		minuteTarget[1] = minuteTargetInt >= 10 ? Integer.toString(minuteTargetInt).charAt(0) : '0';
		minuteTarget[2] = Integer.toString(minuteTargetInt % 10).charAt(0);
		Assert.assertArrayEquals(Arrays.copyOfRange(message.array(), 13, 16), minuteTarget);

		final char[] secondTarget = new char[3];
		final int secondTargetInt = c.get(Calendar.SECOND);
		secondTarget[0] = ':';
		secondTarget[1] = secondTargetInt >= 10 ? Integer.toString(secondTargetInt).charAt(0) : '0';
		secondTarget[2] = Integer.toString(secondTargetInt % 10).charAt(0);
		Assert.assertArrayEquals(Arrays.copyOfRange(message.array(), 16, 19), secondTarget);

		final char[] millisecondTarget = new char[4];
		final int millisecondTargetInt = c.get(Calendar.MILLISECOND);
		millisecondTarget[0] = '.';
		millisecondTarget[1] = millisecondTargetInt >= 100 ? Integer.toString(millisecondTargetInt).charAt(0) : '0';
		if (millisecondTargetInt >= 100) {
			millisecondTarget[2] = Integer.toString(millisecondTargetInt).charAt(1);
		} else if (millisecondTargetInt >= 10) {
			millisecondTarget[2] = Integer.toString(millisecondTargetInt).charAt(0);
		} else {
			millisecondTarget[2] = '0';
		}
		millisecondTarget[3] = Integer.toString(millisecondTargetInt % 10).charAt(0);

		Assert.assertArrayEquals(Arrays.copyOfRange(message.array(), 19, 23), millisecondTarget);

		// System.out.println(message);
	}

	private enum DateLibrary {
		JDK,
		JODA
	}

	private static CharArrayLogMessage getRandomLogMessage(final int numSubArrays, final int subArraysStartIdx) {
		final CharArrayLogMessage randomMsg = new CharArrayLog3rMessage();
		for (int i = subArraysStartIdx; i < (subArraysStartIdx + numSubArrays); i++) {
			final char[] randomSubArray = (char[]) RANDOM_SUBARRAYS[i];
			randomMsg.append(randomSubArray);
		}

		return randomMsg;
	}

	private char[] getMessageSubArray(final CharArrayLogMessage message) {
		return Arrays.copyOfRange(message.array(), 0, message.msgLength());
	}

	private int copyMessageRangeToMasterArray(final CharArrayLogMessage message, final int startPos, final char[] masterArray, final int masterLength) {
		final int copyLength = message.msgLength() - startPos;
		System.arraycopy(message.array(), startPos, masterArray, masterLength, copyLength);
		return copyLength;
	}

	private int appendToLogMessageAndMasterArray(final int i, final CharArrayLogMessage message, final char[] masterArray, final int masterLength) {
		message.append(i);
		final char[] intChars = Integer.toString(i).toCharArray();
		return appendSubArrayToMasterArray(intChars, masterArray, masterLength);
	}

	private int appendToLogMessageAndMasterArray(final long el, final CharArrayLogMessage message, final char[] masterArray, final int masterLength) {
		message.append(el);
		final char[] longChars = Long.toString(el).toCharArray();
		return appendSubArrayToMasterArray(longChars, masterArray, masterLength);
	}

	private int appendToLogMessageAndMasterArray(final double d, final int precision, final CharArrayLogMessage message, final char[] masterArray, final int masterLength) {
		if (precision == -1)
			message.append(d);
		else
			message.append(d, precision);

		final char[] doubleChars = Double.toString(d).toCharArray();
		final char[] charsToAppend;
		if (Double.isNaN(d) || Double.isInfinite(d)) {
			charsToAppend = new char[doubleChars.length + 1];
			System.arraycopy(doubleChars, 0, charsToAppend, 0, doubleChars.length);
			charsToAppend[charsToAppend.length -1] = '.';
		} else {
			charsToAppend = doubleChars;
		}
		return appendSubArrayToMasterArray(charsToAppend, masterArray, masterLength);
	}

	private int appendSubArrayToMasterArray(final char[] subArray, final char[] masterArray, final int startPos) {
		int pos = startPos;
		for (char c : subArray) {
			masterArray[pos++] = c;
		}

		return pos - startPos;
	}
}
