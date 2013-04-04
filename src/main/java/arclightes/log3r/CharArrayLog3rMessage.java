package main.java.arclightes.log3r;

import org.apache.log4j.Logger;

import java.nio.CharBuffer;
import java.util.Calendar;
import java.util.GregorianCalendar;

// Not Thread Safe
class CharArrayLog3rMessage extends BaseLogMessage implements CharArrayMessage {
    private static final Logger log = Logger.getLogger(CharArrayLog3rMessage.class);
	private final NumeralCharArrayBuffer integerBuffer = new NumeralCharArrayBuffer();
	private final DoubleCharArrayBuffer doubleBuffer = new DoubleCharArrayBuffer();
	private final Calendar c = new GregorianCalendar();

    CharArrayLog3rMessage() {
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
			c.setTimeInMillis(msTimestamp);

			integerBuffer.appendInt(c.get(Calendar.YEAR));
			integerBuffer.appendChar('-');
			integerBuffer.appendZeroPaddedNonNegativeInt(c.get(Calendar.MONTH) + 1, 2);
			integerBuffer.appendChar('-');
			integerBuffer.appendZeroPaddedNonNegativeInt(c.get(Calendar.DAY_OF_MONTH), 2);
			integerBuffer.appendChar(' ');
			integerBuffer.appendZeroPaddedNonNegativeInt(c.get(Calendar.HOUR_OF_DAY), 2);
			integerBuffer.appendChar(':');
			integerBuffer.appendZeroPaddedNonNegativeInt(c.get(Calendar.MINUTE), 2);
			integerBuffer.appendChar(':');
			integerBuffer.appendZeroPaddedNonNegativeInt(c.get(Calendar.SECOND), 2);
			integerBuffer.appendChar('.');
			integerBuffer.appendZeroPaddedNonNegativeInt(c.get(Calendar.MILLISECOND), 3);

			msgLength += integerBuffer.copyToDestArrayAndReset(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}
}

