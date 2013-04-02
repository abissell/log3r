package main.java.arclightes.log3r;

import com.lmax.disruptor.Sequence;

// Thread Safe
enum Log3rMessageType implements LogMessageType {
	ARRAY(Log3rSettings.getInstance().getLog3rMessagesQueueSize()) {
		final CharArrayLogMessage constructNewLogMessage() {
			return new CharArrayLog3rMessage();
		}
	};

	private static final long INITIAL_CURSOR_VALUE = -1L;

	private final int indexMask;
	private final Sequence sequence = new Sequence(INITIAL_CURSOR_VALUE);
	private final CharArrayLogMessage[] messages;

	private Log3rMessageType(int queueSize) {
		if (! Log3rUtils.isPowerOfTwo(queueSize)) {
			final int newQueueSize = Log3rUtils.ceilingNextPowerOfTwo(queueSize);
			System.err.println("Tried to initialize main.java.arclightes.log3r.Log3rMessageType with queueSize=" + queueSize +
					           ", resetting to next power of two =" + newQueueSize);
			queueSize = newQueueSize;
		}

		this.indexMask = queueSize - 1;
		this.messages = new CharArrayLogMessage[queueSize];

		for (int i = 0; i < queueSize; i++) {
			this.messages[i] = constructNewLogMessage();
		}
	}

	abstract CharArrayLogMessage constructNewLogMessage();

	public final CharArrayLogMessage getNextMessage() {
		long idx = sequence.incrementAndGet();
		idx = idx & indexMask;
		CharArrayLogMessage message = messages[(int) idx];
//		if (message == null) {
//			message = constructNewLogMessage();
//			messages[(int) idx] = message;
//		}
		message.reset();
		return message;
	}
}
