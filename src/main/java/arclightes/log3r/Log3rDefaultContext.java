package main.java.arclightes.log3r;

import com.lmax.disruptor.*;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

enum Log3rDefaultContext implements LogContext {
	@SuppressWarnings("UnusedDeclaration") INSTANCE; // Enum singleton

	private static final Logger log = Logger.getLogger(Log3rDefaultContext.class);

	private static final String CONTEXT_NAME = Log3rUtil.getDefaultContextName();

	// TODO: Set these by reference to an XML file or other client input
	private static final int HARD_MAX_FILE_LENGTH_MB = 1750;
	private static final int LOG_TARGETS_UPPER_LIMIT = 8;

	private static final int LOG3R_FILE_LENGTH_MB = Math.min(250, HARD_MAX_FILE_LENGTH_MB); // !! Note: Must be <= 1750 to avoid risking overflow on bytes filesize
	private static final int LOG3R_MAX_MESSAGE_LENGTH_CHARS = 131072;
	private static final int LOG3R_INITIAL_MSGDATA_LENGTH = 1024;
	private static final float LOG3R_MSGDATA_LENGTH_LOAD_FACTOR = 1.25f;
    private static final int LOG3R_MESSAGES_QUEUE_SIZE = 1024;
	private static final int FLOATING_POINT_PRECISION_DEFAULT = 5;
	private static final EventFactory<SequencedLogMessage> LOG_MSG_FACTORY = new EventFactory<SequencedLogMessage>() {
		@Override
		public SequencedLogMessage newInstance() {
			return new SequencedLog3rMessage(LOG_TARGETS_UPPER_LIMIT);
		}
	};
	private static final Executor FILE_WRITE_EXECUTOR = Executors.newSingleThreadExecutor();
	private static final ClaimStrategy LOG_MSG_CLAIM_STRATEGY = new MultiThreadedClaimStrategy(LOG3R_MESSAGES_QUEUE_SIZE);
	private static final WaitStrategy FILE_WRITE_WAIT_STRATEGY = new BlockingWaitStrategy();
	private static final EventHandler<SequencedLogMessage> FILE_WRITE_EVENT_HANDLER = new MsgHandler();

	private static final LogTarget DEFAULT_LOG_TARGET = Log3rTargetSingleThread.DEFAULT;

	private static final int EST_MAX_CONSECUTIVE_BYTEBUF_WRITES = 4;
	private static final Map<LogTarget, IntPair<byte[]>> byteBufs = new HashMap<>();

	static Log3rDefaultContext getInstance() {
		return INSTANCE;
	}

	public final String contextName() {
		return CONTEXT_NAME;
	}

    public final int getFileLengthMb() {
        return LOG3R_FILE_LENGTH_MB;
    }

    public final int getMaxMessageLengthChars() {
        return LOG3R_MAX_MESSAGE_LENGTH_CHARS;
    }

	public final int getInitialMsgDataLength() {
		return LOG3R_INITIAL_MSGDATA_LENGTH;
	}

	public final float getMsgDataLengthLoadFactor() {
		return LOG3R_MSGDATA_LENGTH_LOAD_FACTOR;
	}

    public final int getMessagesQueueSize() {
        return LOG3R_MESSAGES_QUEUE_SIZE;
    }

	public final int getFloatingPointPrecisionDefault() {
		return FLOATING_POINT_PRECISION_DEFAULT;
	}

	public final EventFactory<SequencedLogMessage> getLogMsgFactory() {
		return LOG_MSG_FACTORY;
	}

	public final Executor getFileWriteExecutor() {
		return FILE_WRITE_EXECUTOR;
	}

	public final ClaimStrategy getLogMsgClaimStrategy() {
		return LOG_MSG_CLAIM_STRATEGY;
	}

	public final WaitStrategy getFileWriteWaitStrategy() {
		return FILE_WRITE_WAIT_STRATEGY;
	}

	public final EventHandler<SequencedLogMessage> getFileWriteEventHandler() {
	 	return FILE_WRITE_EVENT_HANDLER;
	}

	public final LogTarget getDefaultLogTarget() {
		return DEFAULT_LOG_TARGET;
	}

	private static final /* inner */ class MsgHandler implements EventHandler<SequencedLogMessage> {
		public final void onEvent(final SequencedLogMessage message, final long sequence, final boolean endOfBatch) throws Exception {
			write(message, endOfBatch);
		}
	}

	private static void write(final SequencedLogMessage message, final boolean endOfBatch) {
		try {
			final char[] charArr = message.m().array();
			final int length = message.m().msgLength();
			for (int i = 0; i < message.getNumTargets(); i++) {
				LogTarget target = message.getLogTargets()[i];
				final IntPair<byte[]> byteBuf = getByteBuf(target);
				assureByteBufCapacity(byteBuf, length);
				Log3rUtil.charArrayToByteArray(length, charArr, byteBuf.getVal2(), byteBuf.getVal1());
				if (endOfBatch) {
					final OutputStream outputFile = target.getLogOutput();
					outputFile.write(byteBuf.getVal2(), 0, byteBuf.getVal1() + length);
					outputFile.write('\n');
					outputFile.flush();
					byteBuf.setVal1(0);
				} else {
					byteBuf.getVal2()[byteBuf.getVal1()] = '\n';
					byteBuf.setVal1(byteBuf.getVal1() + 1);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private static void assureByteBufCapacity(final IntPair<byte[]> byteBuf, final int length) {
		int bufCapacity = byteBuf.getVal2().length, loopCount = 0;
		while (byteBuf.getVal1() + length > bufCapacity) {
			bufCapacity *= 2;
			byteBuf.setVal2(new byte[bufCapacity]);
			if (++loopCount > 10)
				throw new ArrayIndexOutOfBoundsException("length " + length + ", bufCapacity " + bufCapacity + ", bufPosition " + byteBuf.getVal1());
		}
	}

	private static IntPair<byte[]> getByteBuf(final LogTarget target) {
		IntPair<byte[]> byteBuf = byteBufs.get(target);
		if (byteBuf == null) {
			synchronized (byteBufs) {
				byteBuf = byteBufs.get(target);
				if (byteBuf == null) {
					final int byteBufLength = EST_MAX_CONSECUTIVE_BYTEBUF_WRITES * LOG3R_MAX_MESSAGE_LENGTH_CHARS + EST_MAX_CONSECUTIVE_BYTEBUF_WRITES;
					final byte[] newBuf = new byte[byteBufLength];
					byteBuf = new IntPair<>(0, newBuf);
					byteBufs.put(target, byteBuf);
				}
			}
		}

		return byteBuf;
	}

	private static final class IntPair<T> {
		private int i;
		private Object t;

		private IntPair(final int i, final T t) {
			this.i = i;
			this.t = t;
		}

		private int getVal1() {
			return i;
		}

		private T getVal2() {
			//noinspection unchecked
			return ((T) t);
		}

		private void setVal1(final int i) {
			this.i = i;
		}

		private void setVal2(final T t) {
			this.t = t;
		}
	}
}
