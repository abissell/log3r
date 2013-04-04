package main.java.arclightes.log3r;

import org.apache.log4j.Logger;
import org.joda.time.Chronology;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableDateTime;
import org.joda.time.chrono.ISOChronology;

import java.nio.CharBuffer;

// Not Thread Safe
final class JodaCharArrayLog3rMessage extends BaseLogMessage implements CharArrayMessage {
    private static final Logger log = Logger.getLogger(JodaCharArrayLog3rMessage.class);
	private static final Chronology CHRON = ISOChronology.getInstance();
	private final NumeralCharArrayBuffer integerBuffer = new NumeralCharArrayBuffer();
	private final DoubleCharArrayBuffer doubleBuffer = new DoubleCharArrayBuffer();
	private final ReadWritableDateTime dateTime = new MutableDateTime(CHRON);

    JodaCharArrayLog3rMessage() {
		super();
    }

	public final CharArrayMessage append(final LogMessage msg) {
		try {
			append(msg.array(), 0, msg.msgLength());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final CharBlock block) {
		try {
			append(block.array());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final char c) {
		try {
			array[msgLength++] = c;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final char[] srcArray) {
		try {
			System.arraycopy(srcArray, 0, array, msgLength, srcArray.length);
			msgLength += srcArray.length;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final char[] srcArray, final int srcPos, final int length) {
		try {
			System.arraycopy(srcArray, srcPos, array, msgLength, length);
			msgLength += length;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final CharBuffer srcBuffer) {
		try {
			final int srcBufferLength = srcBuffer.length();
			if (srcBufferLength > 0) {
				srcBuffer.get(array, msgLength, srcBufferLength);
				msgLength += srcBufferLength;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final int i) {
		try {
			integerBuffer.appendInt(i);
			msgLength += integerBuffer.copyToDestArrayAndReset(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final long el) {
		try {
			integerBuffer.appendLong(el);
			msgLength += integerBuffer.copyToDestArrayAndReset(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final double d) {
		try {
			doubleBuffer.appendDouble(d);
			msgLength += doubleBuffer.copyToDestArrayAndReset(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final double d, final int precision) {
		try {
			doubleBuffer.appendDouble(d, precision);
			msgLength += doubleBuffer.copyToDestArrayAndReset(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage appendMillisecondTimestamp(final long msTimestamp) {
		try {
			dateTime.setMillis(msTimestamp);

			integerBuffer.appendInt(dateTime.getYear());
			integerBuffer.appendChar('-');
			integerBuffer.appendZeroPaddedNonNegativeInt(dateTime.getMonthOfYear(), 2);
			integerBuffer.appendChar('-');
			integerBuffer.appendZeroPaddedNonNegativeInt(dateTime.getDayOfMonth(), 2);
			integerBuffer.appendChar(' ');
			integerBuffer.appendZeroPaddedNonNegativeInt(dateTime.getHourOfDay(), 2);
			integerBuffer.appendChar(':');
			integerBuffer.appendZeroPaddedNonNegativeInt(dateTime.getMinuteOfHour(), 2);
			integerBuffer.appendChar(':');
			integerBuffer.appendZeroPaddedNonNegativeInt(dateTime.getSecondOfMinute(), 2);
			integerBuffer.appendChar('.');
			integerBuffer.appendZeroPaddedNonNegativeInt(dateTime.getMillisOfSecond(), 3);

			msgLength += integerBuffer.copyToDestArrayAndReset(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}
}

