package main.java.arclightes.log3r;

import org.apache.log4j.Logger;

import java.nio.CharBuffer;

// Not Thread Safe
class CharArrayLog3rMessage extends BaseLogMessage implements CharArrayMessage {
    private static final Logger log = Logger.getLogger(CharArrayLog3rMessage.class);
	private final BulkNumeralCharAppender integerBuffer = new NumeralCharArrayBuffer();
	private final FlushableDoubleCharAppender doubleBuffer = new DoubleCharArrayBuffer();
	private final TimestampCharAppender timestampAppender = new GregorianTimestampCharAppender(integerBuffer);

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
			msgLength += integerBuffer.flush(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final long el) {
		try {
			integerBuffer.appendLong(el);
			msgLength += integerBuffer.flush(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final double d) {
		try {
			doubleBuffer.appendDouble(d);
			msgLength += doubleBuffer.flush(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage append(final double d, final int precision) {
		try {
			doubleBuffer.appendDouble(d, precision);
			msgLength += doubleBuffer.flush(array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}

	public final CharArrayMessage appendMillisecondTimestamp(final long msTimestamp) {
		try {
			msgLength += timestampAppender.appendMillisecondTimestamp(msTimestamp, array, msgLength);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return this;
	}
}

