package main.java.arclightes.log3r;

import org.junit.Test;

public class TestCachedTimestampAppender {
	@Test
	public void testCachedTimestampAppender() {
		final BulkNumeralCharAppender numeralAppender = new NumeralCharArrayBuffer();
		final CachingTimestampCharAppender appender = new CachingTimestampCharAppender(numeralAppender);
		final char[] destArray = new char[60];

		long timeMs = System.currentTimeMillis();
		appender.appendMillisecondTimestamp(timeMs, destArray, 0);
		printTimestampCharacteristics(timeMs, destArray, appender);

		timeMs += 6;
		appender.appendMillisecondTimestamp(timeMs, destArray, 0);
		printTimestampCharacteristics(timeMs, destArray, appender);

		timeMs += 1001;
		appender.appendMillisecondTimestamp(timeMs, destArray, 0);
		printTimestampCharacteristics(timeMs, destArray, appender);

		timeMs += 1000 * 60;
		appender.appendMillisecondTimestamp(timeMs, destArray, 0);
		printTimestampCharacteristics(timeMs, destArray, appender);

		timeMs += 1000 * 60 * 60;
		appender.appendMillisecondTimestamp(timeMs, destArray, 0);
		printTimestampCharacteristics(timeMs, destArray, appender);
	}

	private void printTimestampCharacteristics(final long timeMs,
											   final char[] destArray,
											   final CachingTimestampCharAppender appender) {
		System.out.println(destArray);
		System.out.println("Millis to next sec: " + (appender.getNextSecond() - timeMs));
		System.out.println("Millis to next min: " + (appender.getNextMinute() - timeMs));
		System.out.println("Millis to next hr : " + (appender.getNextHour() - timeMs));
	}
}
