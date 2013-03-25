import net.jcip.annotations.NotThreadSafe;
import org.apache.log4j.Logger;

import java.nio.CharBuffer;
import java.util.Calendar;
import java.util.GregorianCalendar;

@NotThreadSafe
final class BufferLogMessage extends BasicArrayLogMessage implements CharBufferLogMessage {
    private static final Logger log = Logger.getLogger(BufferLogMessage.class);
	private final LongCharBuffer longCharBuffer = new LongCharBuffer();
	private final IntCharBuffer intCharBuffer = new IntCharBuffer();
	private final DoubleCharBuffer doubleCharBuffer = new DoubleCharBuffer();
	private final Calendar c = new GregorianCalendar();
	private final CharBuffer buffer;

    BufferLogMessage() {
        super();
		this.buffer = CharBuffer.wrap(array);
    }

	@Override
	public final LogMessage append(final char c) {
		try
		{
			msgLength++;
			buffer.append(c);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return this;
	}

	@Override
	public final LogMessage append(final char[] array) {
		try
		{
			msgLength += array.length;
			for (char c : array) {
				buffer.append(c);
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return this;
	}

	@Override
	public final LogMessage append(final char[] srcArray, final int from, final int to) {
		try {
			msgLength += (to - from);
			for (int i = from; i < to; i++) {
				buffer.append(srcArray[i]);
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return this;
	}

	@Override
	public final LogMessage append(final CharBuffer srcBuffer) {
		try
		{
			msgLength += srcBuffer.length();
			for (int i = 0; i < srcBuffer.length(); i++) {
				buffer.append(srcBuffer.get(i));
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return this;
	}

	@Override
	public final void reset() {
		buffer.clear();
		buffer.rewind();
		super.reset();
	}

	// TODO: Wrap with UnmodifiableDecorator
	public final CharBuffer buffer() {
		return buffer;
	}

	public final LogMessage appendMillisecondTimestamp(final long msTime) {
		try
		{
			msgLength += Log3rUtils.dateToBuffer(msTime, c, intCharBuffer, buffer);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return this;
	}

	public final LogMessage append(final int i) {
		try
		{
			Log3rUtils.intToCharBuffer(intCharBuffer, i);
			msgLength += intCharBuffer.copyToBufferAndReset(buffer);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return this;
	}

	public final LogMessage append(final long l) {
		try
		{
			Log3rUtils.longToCharBuffer(longCharBuffer, l);
			msgLength += longCharBuffer.copyToBufferAndReset(buffer);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return this;
	}

	public final LogMessage append(final double d) {
		try
		{
			Log3rUtils.doubleToCharBuffer(doubleCharBuffer, d);
			msgLength += doubleCharBuffer.copyToBufferAndReset(buffer);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return this;
	}

    // TODO: Build this out
	public final LogMessage append(final double d, final int precision) {
        /*
        try
        {
            Log3rUtils.doubleToCharBuffer(doubleCharBuffer, d, precision);
            msgLength += doubleCharBuffer.copyToBufferAndReset(buffer);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return this;
        */
        return append(d);
    }
}

