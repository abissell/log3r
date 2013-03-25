import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
enum BasicLogMessageType implements LogMessageType {
	ARRAY(SettingDefaults.getPerfLogQueueSize()) {
		final LogMessage constructNewLogMessage() {
			return new BasicArrayLogMessage();
		}
	},

	BUFFER(SettingDefaults.getPerfLogQueueSize()) {
		final LogMessage constructNewLogMessage() {
			return new BufferLogMessage();
		}
	};

	private final int queueSize;
	private final AtomicInteger msgIdx = new AtomicInteger(0);
	private final LogMessage[] messages;

	private BasicLogMessageType(final int queueSize) {
		if (! Log3rUtils.isPowerOfTwo(queueSize))
			throw new IllegalArgumentException("queueSize must be power of 2!");

		this.queueSize = queueSize;
		this.messages = new LogMessage[this.queueSize];
	}

	abstract LogMessage constructNewLogMessage();

	public final LogMessage getNextMessage() {
		int idx = msgIdx.getAndIncrement();
		if (idx == Integer.MAX_VALUE)
			msgIdx.set(0);

		idx = idx % queueSize;
		LogMessage message = messages[idx];
		if (message == null)
		{
			message = constructNewLogMessage();
			messages[idx] = message;
		}
		message.reset();
		return message;
	}
}
