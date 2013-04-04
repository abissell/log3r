package main.java.arclightes.log3r;

// Thread Safe
enum PaddedLog3rMessageType implements PaddedLogMessageType {
	PADDED_ARRAY(Log3rDefaultContext.getInstance().getMessagesQueueSize()) {
		final CharArrayMessage constructNewLogMessage() {
			return new PaddedCharArrayLog3rMessage();
		}
	};

	private final MessagePool<CharArrayMessage> messagePool;

	private PaddedLog3rMessageType(int queueSize) {
		if (! Log3rUtil.isPowerOfTwo(queueSize)) {
			final int newQueueSize = Log3rUtil.ceilingNextPowerOfTwo(queueSize);
			System.err.println("Tried to initialize main.java.arclightes.log3r.Log3rMessageType with queueSize=" + queueSize +
					", resetting to next power of two =" + newQueueSize);
			queueSize = newQueueSize;
		}

		final CharArrayMessage[] messagesArr = new CharArrayMessage[queueSize];
		for (int i = 0; i < messagesArr.length; i++) {
			messagesArr[i] = constructNewLogMessage();
		}
		this.messagePool = new Log3rMessagePool<>(queueSize, messagesArr);
	}

	abstract CharArrayMessage constructNewLogMessage();

	public final CharArrayMessage getNextMessage() {
		return messagePool.getNextMessage();
	}

	public final long sumPaddingToPreventOptimization(final int index) {
		final PaddedCharArrayLog3rMessage m = (PaddedCharArrayLog3rMessage) messagePool.getNextMessage();
		return m.p1 + m.p2 + m.p3 + m.p4 + m.p5 + m.p6;
	}
}
