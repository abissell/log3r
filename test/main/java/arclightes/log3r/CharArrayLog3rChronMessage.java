package main.java.arclightes.log3r;

import org.apache.log4j.Logger;

import java.nio.CharBuffer;
import java.util.Calendar;
import java.util.GregorianCalendar;

// Not Thread Safe
final class CharArrayLog3rChronMessage extends BaseLogMessage implements CharArrayMessage {
    private static final Logger log = Logger.getLogger(CharArrayLog3rChronMessage.class);
	private final Calendar c = new GregorianCalendar();

    CharArrayLog3rChronMessage() {
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
			msgLength += JavaChronicleAppender.appendInt(i, array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final long el) {
		try {
			msgLength += JavaChronicleAppender.appendLong(el, array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final double d) {
		try {
			msgLength += JavaChronicleAppender.appendDouble(d, array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final double d, final int precision) {
		try {
			msgLength += JavaChronicleAppender.appendDouble(d, precision, array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage appendMillisecondTimestamp(final long msTimestamp) {
		try {
			c.setTimeInMillis(msTimestamp);

			msgLength += JavaChronicleAppender.appendInt(c.get(Calendar.YEAR), array, msgLength);
			array[msgLength++] = '-';
			msgLength += JavaChronicleAppender.appendZeroPaddedNonNegativeInt(
					c.get(Calendar.MONTH) + 1, 2, array, msgLength);
			array[msgLength++] = '-';
			msgLength += JavaChronicleAppender.appendZeroPaddedNonNegativeInt(
					c.get(Calendar.DAY_OF_MONTH), 2, array, msgLength);
			array[msgLength++] = ' ';
			msgLength += JavaChronicleAppender.appendZeroPaddedNonNegativeInt(
					c.get(Calendar.HOUR_OF_DAY), 2, array, msgLength);
			array[msgLength++] = ':';
			msgLength += JavaChronicleAppender.appendZeroPaddedNonNegativeInt(
					c.get(Calendar.MINUTE), 2, array, msgLength);
			array[msgLength++] = ':';
			msgLength += JavaChronicleAppender.appendZeroPaddedNonNegativeInt(
					c.get(Calendar.SECOND), 2, array, msgLength);
			array[msgLength++] = '.';
			msgLength += JavaChronicleAppender.appendZeroPaddedNonNegativeInt(
					c.get(Calendar.MILLISECOND), 3, array, msgLength);

			return this;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}
}

