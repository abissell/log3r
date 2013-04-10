package main.java.arclightes.log3r;

import java.util.Calendar;
import java.util.GregorianCalendar;

final class GregorianTimestampCharAppender implements TimestampCharAppender {
	private final Calendar c = new GregorianCalendar();
	private final BulkNumeralCharAppender numeralAppender;
	
	GregorianTimestampCharAppender(final BulkNumeralCharAppender numeralAppender) {
		this.numeralAppender = numeralAppender;	
	}
	
	public final GregorianTimestampCharAppender appendMillisecondTimestamp(final long msTimestamp) {

		c.setTimeInMillis(msTimestamp);
		
		numeralAppender.appendInt(c.get(Calendar.YEAR));
		numeralAppender.appendChar('-');
		numeralAppender.appendZeroPaddedNonNegativeInt(c.get(Calendar.MONTH) + 1, 2);
		numeralAppender.appendChar('-');
		numeralAppender.appendZeroPaddedNonNegativeInt(c.get(Calendar.DAY_OF_MONTH), 2);
		numeralAppender.appendChar(' ');
		numeralAppender.appendZeroPaddedNonNegativeInt(c.get(Calendar.HOUR_OF_DAY), 2);
		numeralAppender.appendChar(':');
		numeralAppender.appendZeroPaddedNonNegativeInt(c.get(Calendar.MINUTE), 2);
		numeralAppender.appendChar(':');
		numeralAppender.appendZeroPaddedNonNegativeInt(c.get(Calendar.SECOND), 2);
		numeralAppender.appendChar('.');
		numeralAppender.appendZeroPaddedNonNegativeInt(c.get(Calendar.MILLISECOND), 3);

		return this;
	}
}
