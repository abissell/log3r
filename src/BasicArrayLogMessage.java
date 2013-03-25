import net.jcip.annotations.NotThreadSafe;
import org.apache.log4j.Logger;

import java.nio.CharBuffer;

@NotThreadSafe
class BasicArrayLogMessage extends BaseLogMessage implements CharArrayLogMessage {
    private static final Logger log = Logger.getLogger(BasicArrayLogMessage.class);
    
    BasicArrayLogMessage() {

    }

	// Extensible enum example function
//	public final <U extends Enum<U> & CharBlock> void appendAllBlocks(Class<U> blockType) {
//		for (CharBlock block : blockType.getEnumConstants()) {
//			append(block.buffer());
//		}
//	}
	public final LogMessage append(final LogMessage msg) {
		try
		{
			append(msg.array(), 0, msg.msgLength());
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return this;
	}

	public final LogMessage append(final CharBlock block) {
		return append(block.array());
	}

	public LogMessage append(final char c) {
		try
		{
			array[msgLength++] = c;
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return this;
	}

	public LogMessage append(final char[] srcArray) {
		try
		{
			final int priorMsgLength = msgLength;
			msgLength += srcArray.length;
			System.arraycopy(srcArray, 0, array, priorMsgLength, srcArray.length);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return this;
	}

	public LogMessage append(final char[] srcArray, final int from, final int to) {
		try {
			final int priorMsgLength = msgLength;
			msgLength += (to - from);
			System.arraycopy(srcArray, from, array, priorMsgLength, (to - from));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return this;
	}

	public LogMessage append(final CharBuffer srcBuffer) {
		try
		{
			for (int i = 0; i < srcBuffer.length(); i++) {
				append(srcBuffer.get(i));
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return this;
	}
}

