import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import org.apache.log4j.Logger;

import java.io.OutputStream;

// Enum Singleton
public enum PerfLog {
	@SuppressWarnings("UnusedDeclaration")
	INSTANCE;

	private static final Logger log = Logger.getLogger(PerfLog.class);

	private static final int MAX_MSG_LENGTH = SettingDefaults.getPerfLogMaxMsgLength();
    private static final RingBuffer<MsgData> BUFFER;
	static {
		if (SettingDefaults.getPerfLogFileLengthMb() > 1750) // Prevent integer overflow on file size
			throw new IllegalArgumentException("Perf Log max file length in MB must be <= 1750!");
		final EventFactory<MsgData> eventFactory = new EventFactory<MsgData>() {
			@Override
			public MsgData newInstance() {
				return new MsgData();
			}
		};
		final Disruptor<MsgData> disruptor = new Disruptor<>(eventFactory,
				ExecutorFactory.CACHED_THREAD_EXECUTOR,
				new MultiThreadedClaimStrategy(SettingDefaults.getPerfLogQueueSize()),
				new BlockingWaitStrategy());
		disruptor.handleEventsWith(new LogHandler());
		BUFFER = disruptor.start();
	}
    private static final ThreadLocal<byte[]> BYTE_BUF = new ThreadLocal<>();

    public static byte[] getByteBuf() {
        byte[] buf = BYTE_BUF.get();
        if (buf == null)
        {
            buf = new byte[MAX_MSG_LENGTH];
            BYTE_BUF.set(buf);
        }
        //System.arraycopy(BlankBuffer, 0, buf, 0, MAX_MSG_LENGTH);
        return buf;
    }
    
	public static LogMessage getMessage(final LogMessageType type) {
        return type.getNextMessage();
    }

    public void log(final LogMessage message) {
        log(LogTargetImpl.DEFAULT, message);
    }

    public void log(final LogTarget logTarget, final LogMessage message) {
        final char[] charArr = message.array();
        final int msgLen = message.msgLength();
        final long sequence = BUFFER.next();
        final MsgData msgData = BUFFER.get(sequence);
        if (msgData.data == null || msgData.length < msgLen)
            msgData.data = new char[msgLen];
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
        try
        {

            final char[] charArr = message.data;
            final int length = message.length;
            final byte[] bytes = getByteBuf();
            Log3rUtils.charArrayToByteArray(length, charArr, bytes);
            final LogTarget logTarget = message.target;
            try
            {
				logTarget.lockLog();
                final OutputStream outputFile = logTarget.getLogOutput();
                outputFile.write(bytes, 0, length);
                outputFile.write('\n');
                outputFile.flush();
            }
            finally {
            	logTarget.unlockLog();
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    private static final /* inner */ class MsgData {
        private char[] data;
        private int length;
        private LogTarget target;
    }
}
