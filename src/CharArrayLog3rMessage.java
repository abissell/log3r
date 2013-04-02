import org.apache.log4j.Logger;

import java.nio.CharBuffer;
import java.util.Calendar;
import java.util.GregorianCalendar;

// Not Thread Safe
final class CharArrayLog3rMessage extends BaseLogMessage implements CharArrayLogMessage {
    private static final Logger log = Logger.getLogger(CharArrayLog3rMessage.class);
	private final NumeralCharArrayBuffer integerBuffer = new NumeralCharArrayBuffer();
	private final DoubleCharArrayBuffer doubleBuffer = new DoubleCharArrayBuffer();
	private final Calendar c = new GregorianCalendar();

    CharArrayLog3rMessage() {
		super();
    }

	public final LogMessage append(final LogMessage msg) {
		try {
			append(msg.array(), 0, msg.msgLength());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final LogMessage append(final CharBlock block) {
		try {
			append(block.array());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final LogMessage append(final char c) {
		try {
			array[msgLength++] = c;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final LogMessage append(final char[] srcArray) {
		try {
			final int priorMsgLength = msgLength;
			msgLength += srcArray.length;
			System.arraycopy(srcArray, 0, array, priorMsgLength, srcArray.length);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final LogMessage append(final char[] srcArray, final int from, final int to) {
		try {
			final int priorMsgLength = msgLength;
			final int appendedLength = to - from;
			msgLength += appendedLength;
			System.arraycopy(srcArray, from, array, priorMsgLength, appendedLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final LogMessage append(final CharBuffer srcBuffer) {
		try {
			final int srcBufferLength = srcBuffer.length();
			if (srcBufferLength > 0) {
				msgLength += srcBufferLength;
				srcBuffer.get(array, msgLength, srcBufferLength);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final LogMessage append(final int i) {
		try {
			integerBuffer.appendInt(i);
			msgLength += integerBuffer.copyToDestArrayAndReset(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final LogMessage append(final long el) {
		try {
			integerBuffer.appendLong(el);
			msgLength += integerBuffer.copyToDestArrayAndReset(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final LogMessage append(final double d) {
		try {
			doubleBuffer.appendDouble(d);
			msgLength += doubleBuffer.copyToDestArrayAndReset(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final LogMessage append(final double d, final int precision) {
		try {
			doubleBuffer.appendDouble(d, precision);
			msgLength += doubleBuffer.copyToDestArrayAndReset(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final LogMessage appendMillisecondTimestamp(final long msTimestamp) {
		try {
			c.setTimeInMillis(msTimestamp);

			integerBuffer.appendInt(c.get(Calendar.YEAR));
			integerBuffer.appendChar('-');
			integerBuffer.appendZeroPaddedNonNegativeInt(c.get(Calendar.MONTH), 2);
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

