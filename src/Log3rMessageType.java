import com.lmax.disruptor.Sequence;

// Thread Safe
enum Log3rMessageType implements LogMessageType {
	ARRAY(Log3rSettings.getInstance().getLog3rMessagesQueueSize()) {
		final LogMessage constructNewLogMessage() {
			return new CharArrayLog3rMessage();
		}
	};

	private static final long INITIAL_CURSOR_VALUE = -1L;

	private final int indexMask;
	private final Sequence sequence = new Sequence(INITIAL_CURSOR_VALUE);
	private final LogMessage[] messages;

	private Log3rMessageType(int queueSize) {
		if (! Log3rUtils.isPowerOfTwo(queueSize)) {
			final int newQueueSize = Log3rUtils.ceilingNextPowerOfTwo(queueSize);
			System.err.println("Tried to initialize Log3rMessageType with queueSize=" + queueSize +
					           ", resetting to next power of two =" + newQueueSize);
			queueSize = newQueueSize;
		}

		this.indexMask = queueSize - 1;
		this.messages = new LogMessage[queueSize];
	}

	abstract LogMessage constructNewLogMessage();

	public final LogMessage getNextMessage() {
		long idx = sequence.incrementAndGet();
		idx = idx & indexMask;
		LogMessage message = messages[(int) idx];
		if (message == null) {
			message = constructNewLogMessage();
			messages[(int) idx] = message;
		}
		message.reset();
		return message;
	}
}
