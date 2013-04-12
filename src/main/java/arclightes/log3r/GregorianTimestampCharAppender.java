package main.java.arclightes.log3r;

import java.util.Calendar;
import java.util.GregorianCalendar;

final class GregorianTimestampCharAppender implements TimestampCharAppender {
	private final Calendar c = new GregorianCalendar();
	private final BulkNumeralCharAppender numeralAppender;
	
	GregorianTimestampCharAppender(final BulkNumeralCharAppender numeralAppender) {
		this.numeralAppender = numeralAppender;	
	}
	
	public final int appendMillisecondTimestamp(final long msTimestamp,
												final char[] destArray,
												int destPos) {

		c.setTimeInMillis(msTimestamp);

		Log3rUtil.appendTimestamp(c, numeralAppender);

		return numeralAppender.flush(destArray, destPos);
	}
}
