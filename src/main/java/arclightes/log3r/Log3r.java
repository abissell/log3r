package main.java.arclightes.log3r;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import org.apache.log4j.Logger;

import java.io.OutputStream;

@SuppressWarnings("FinalStaticMethod")
public enum Log3r {
	@SuppressWarnings("UnusedDeclaration") INSTANCE; // Enum singleton

	private static final Logger log = Logger.getLogger(Log3r.class);

	private static final float MSGDATA_LENGTH_LOAD_FACTOR = Log3rSettings.getInstance().getLog3rMsgDataLengthLoadFactor();
	private static final int MAX_MSG_LENGTH = Log3rSettings.getInstance().getLog3rMaxMessageLengthChars();
    private static final RingBuffer<MsgData> BUFFER;
	static {
		if (Log3rSettings.getInstance().getLog3rFileLengthMb() > 1750) // Prevent integer overflow on file size
			throw new IllegalArgumentException("Perf Log max file length in MB must be <= 1750!");

		final EventFactory<MsgData> eventFactory = new EventFactory<MsgData>() {
			@Override
			public MsgData newInstance() {
				return new MsgData();
			}
		};
		final Disruptor<MsgData> disruptor = new Disruptor<>(eventFactory,
				Log3rExecutorFactory.CACHED_THREAD_EXECUTOR,
				new MultiThreadedClaimStrategy(Log3rSettings.getInstance().getLog3rMessagesQueueSize()),
				new BlockingWaitStrategy());
		disruptor.handleEventsWith(new LogHandler());
		BUFFER = disruptor.start();
	}
    private static final ThreadLocal<byte[]> BYTE_BUF = new ThreadLocal<>();

	public static final Log3r getInstance() {
		return INSTANCE;
	}

    private static byte[] getByteBuf() {
        byte[] buf = BYTE_BUF.get();
        if (buf == null)
        {
            buf = new byte[MAX_MSG_LENGTH];
            BYTE_BUF.set(buf);
        }
        return buf;
    }
    
	private static LogMessage getMessage(final LogMessageType type) {
        return type.getNextMessage();
    }

    private static void log(final LogMessage message) {
        log(Log3rTarget.DEFAULT, message);
    }

    private static void log(final LogTarget logTarget, final LogMessage message) {
        final char[] charArr = message.array();
        final int msgLen = message.msgLength();
        final long sequence = BUFFER.next();
        final MsgData msgData = BUFFER.get(sequence);
        if (msgLen > msgData.length) {
            final int newMsgDataLen = (int) (MSGDATA_LENGTH_LOAD_FACTOR * msgLen);
			msgData.data = new char[newMsgDataLen];
		}
        msgData.length = msgLen;
        msgData.target = logTarget;
        System.arraycopy(charArr, 0, msgData.data, 0, msgLen);
        BUFFER.publish(sequence);
    }

    private static final  /* inner */ class LogHandler implements EventHandler<MsgData> {
        public void onEvent(final MsgData logMessage, final long sequence, final boolean endOfBatch) throws Exception {
            write(logMessage);
        }
    }
    
    private static void write(final MsgData message) {
        try {
            final char[] charArr = message.data;
            final int length = message.length;
            final byte[] bytes = getByteBuf();
            Log3rUtils.charArrayToByteArray(length, charArr, bytes);
            final LogTarget logTarget = message.target;
			logTarget.lockLog();
			try {
                final OutputStream outputFile = logTarget.getLogOutput();
                outputFile.write(bytes, 0, length);
                outputFile.write('\n');
                outputFile.flush();
            } finally {
            	logTarget.unlockLog();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static final /* inner */ class MsgData {
        private char[] data = new char[Log3rSettings.getInstance().getLog3rInitialMsgDataLength()];
        private int length = data.length;
        private LogTarget target;

		private MsgData() {

		}
    }
}
