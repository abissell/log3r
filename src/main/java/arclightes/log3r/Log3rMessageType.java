package main.java.arclightes.log3r;

// Thread Safe
enum Log3rMessageType implements LogMessageType {
	ARRAY(Log3rDefaultContext.getInstance().getMessagesQueueSize()) {
		final CharArrayMessage constructNewLogMessage() {
			return new CharArrayLog3rMessage();
		}
	};

	private final MessagePool<CharArrayMessage> messagePool;

	private Log3rMessageType(int queueSize) {
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
}
