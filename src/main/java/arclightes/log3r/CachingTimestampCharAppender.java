package main.java.arclightes.log3r;

import java.util.Calendar;
import java.util.GregorianCalendar;

final class CachingTimestampCharAppender implements TimestampCharAppender {
	private static final long MILLIS_PER_SECOND = 1000L;
	private static final long MILLIS_PER_MINUTE = MILLIS_PER_SECOND * 60L;

	private final Calendar c = new GregorianCalendar();
	private final BulkNumeralCharAppender numeralAppender;

	// Cached values
	private long cachedTimeMs = 0L;
	private int millisecond = 0;
	private int second = 0;
	private int minute = 0;
	private int hourOfDay = 0;
	private int day = 0;
	private int month = 0;
	private int year = 0;

	private long nextSecond = 0L;
	private long nextMinute = 0L;
	private long nextHour = 0L;

	CachingTimestampCharAppender(final BulkNumeralCharAppender numeralAppender) {
		this.numeralAppender = numeralAppender;	
	}
	
	public final int appendMillisecondTimestamp(final long msTimestamp,
												final char[] destArray,
												final int destPos) {

		if (msTimestamp < cachedTimeMs)
			recacheAll(msTimestamp);

		if (msTimestamp < nextSecond) {
			millisecond += msTimestamp - cachedTimeMs;
		} else if (msTimestamp < nextMinute) {
			final long millisAfterOldNextSecond = msTimestamp - nextSecond;
			millisecond = (int) (millisAfterOldNextSecond % MILLIS_PER_SECOND);

			final int secondsAfterOldNextSecond = (int) (millisAfterOldNextSecond / MILLIS_PER_SECOND);
			second = (second + 1 + secondsAfterOldNextSecond) % 60;
			nextSecond = nextSecond + (secondsAfterOldNextSecond + 1) * MILLIS_PER_SECOND;
		} else if (msTimestamp < nextHour) {
			final long millisAfterOldNextMinute = msTimestamp - nextMinute;
			millisecond = (int) (millisAfterOldNextMinute % MILLIS_PER_SECOND);

			final int secondsAfterOldNextMinute = (int) (millisAfterOldNextMinute / MILLIS_PER_SECOND);
			second = secondsAfterOldNextMinute;
			nextSecond = nextMinute + (secondsAfterOldNextMinute + 1) * MILLIS_PER_SECOND;

			final int minutesAfterOldNextMinute = (int) (millisAfterOldNextMinute / MILLIS_PER_MINUTE);
			minute = (minute + 1 + minutesAfterOldNextMinute) % 60;
			nextMinute = nextMinute + (minutesAfterOldNextMinute + 1) * MILLIS_PER_MINUTE;
		} else { // Thoroughly recache all fields every hour on the hour
			recacheAll(msTimestamp);
		}

		cachedTimeMs = msTimestamp;

		Log3rUtil.appendTimestamp(year, month, day, hourOfDay,
							      minute, second, millisecond, numeralAppender);

		return numeralAppender.flush(destArray, destPos);
	}

	private void recacheAll(final long msTimestamp) {
		c.setTimeInMillis(msTimestamp);

		millisecond = c.get(Calendar.MILLISECOND);
		final int millisMargin = 1000 - millisecond;
		nextSecond = msTimestamp + millisMargin;

		second = c.get(Calendar.SECOND);
		final int secondsMargin = 60 - second - 1;
		nextMinute = nextSecond + (secondsMargin * MILLIS_PER_SECOND);

		minute = c.get(Calendar.MINUTE);
		final int minutesMargin = 60 - minute - 1;
		nextHour = nextMinute + (minutesMargin * MILLIS_PER_MINUTE);

		hourOfDay = c.get(Calendar.HOUR_OF_DAY);
		day = c.get(Calendar.DAY_OF_MONTH);
		month = c.get(Calendar.MONTH) + 1;
		year = c.get(Calendar.YEAR);
	}

	long getNextSecond() {
		return nextSecond;
	}

	long getNextMinute() {
		return nextMinute;
	}

	long getNextHour() {
		return nextHour;
	}
}
