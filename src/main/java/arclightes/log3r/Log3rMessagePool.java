package main.java.arclightes.log3r;

import com.lmax.disruptor.Sequence;

class Log3rMessagePool<T extends LogMessage> implements MessagePool<T> {
	private final int indexMask;
	private final Sequence sequence = new Sequence();
	private final T[] messages;

	Log3rMessagePool(int queueSize, final T[] constructedMessages) {
		if (! Log3rUtil.isPowerOfTwo(queueSize)) {
			final int newQueueSize = Log3rUtil.ceilingNextPowerOfTwo(queueSize);
			System.err.println("Tried to initialize main.java.arclightes.log3r.Log3rMessageType with queueSize=" + queueSize +
					", resetting to next power of two =" + newQueueSize);
			queueSize = newQueueSize;
		}

		this.indexMask = queueSize - 1;
		this.messages = constructedMessages;
	}

	public final T getNextMessage() {
		long idx = sequence.incrementAndGet();
		idx = idx & indexMask;
		T message = messages[(int) idx];
		message.reset();
		return message;
	}
}
