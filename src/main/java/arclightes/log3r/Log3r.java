package main.java.arclightes.log3r;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("FinalStaticMethod")
public final class Log3r {
	private static final Logger log = Logger.getLogger(Log3r.class);

	private static final Map<String, Log3r> contexts = new HashMap<>();

	private final RingBuffer<SequencedLogMessage> msgBuffer;

	Log3r(final LogContext config) {
		if (config.getFileLengthMb() > 1750) // Prevent integer overflow on file size
			throw new IllegalArgumentException("Perf Log max file length in MB must be <= 1750!");

		final Disruptor<SequencedLogMessage> inputDisruptor = new Disruptor<>(config.getLogMsgFactory(),
																			  config.getFileWriteExecutor(),
																			  config.getLogMsgClaimStrategy(),
																			  config.getFileWriteWaitStrategy());
		//noinspection unchecked
		inputDisruptor.handleEventsWith(config.getFileWriteEventHandler());
		msgBuffer = inputDisruptor.start();
	}

	public static Log3r getLog3r() {
		return getLog3r(Log3rDefaultContext.getInstance());
	}

	public static Log3r getLog3r(final LogContext context) {
		final String name = context.contextName();
		Log3r log3r = contexts.get(name);
		if (log3r == null) {
			synchronized (contexts) {
				log3r = contexts.get(name);
				if (log3r == null) {
					log3r = new Log3r(Log3rDefaultContext.getInstance());
					contexts.put(name, log3r);
				}
			}
		}

		return log3r;
	}

	public final SequencedLogMessage getSequencedLogMessage(final LogMessageType type) {
		final long sequence = msgBuffer.next();
		final SequencedLogMessage message = msgBuffer.get(sequence);
		message.prepForWrite(sequence, type);
		return message;
	}

	public final LogMessage getMessage(final LogMessageType type) {
        return type.getNextMessage();
    }

    public final void log(final SequencedLogMessage message) {
		final long sequence = message.getSequence();
		msgBuffer.publish(sequence);
    }
}
