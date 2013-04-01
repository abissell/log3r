import com.lmax.disruptor.Sequence;

// Thread Safe
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

	private final int indexMask;
	private static final long INITIAL_CURSOR_VALUE = -1L;
	private final Sequence sequence = new Sequence(INITIAL_CURSOR_VALUE);
	private final LogMessage[] messages;

	private BasicLogMessageType(final int queueSize) {
		if (! Log3rAppendUtils.isPowerOfTwo(queueSize))
			throw new IllegalArgumentException("queueSize must be power of 2!");

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
