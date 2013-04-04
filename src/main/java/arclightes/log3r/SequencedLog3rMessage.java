package main.java.arclightes.log3r;

final class SequencedLog3rMessage implements SequencedLogMessage {
	private long sequence;
	private LogMessage message;
	private LogTarget[] targets;
	private int numTargets = 0;

	SequencedLog3rMessage(final int targetListSize) {
		targets = new LogTarget[targetListSize];
	}

	public final void prepForWrite(final long sequence, final LogMessageType type) {
		this.sequence = sequence;
		this.message = type.getNextMessage();
	}

	public final LogMessage m() {
		return message;
	}

	public final SequencedLog3rMessage addLogTarget(final LogTarget target) {
		if (++numTargets > targets.length) {
			LogTarget[] newArray = new LogTarget[targets.length * 2];
			System.arraycopy(targets, 0, newArray, 0, targets.length);
			targets = newArray;
		}

		targets[numTargets - 1] = target;

		return this;
	}

	public final long getSequence() {
		return sequence;
	}

	public final LogTarget[] getLogTargets() {
		return targets;
	}

	public final int getNumTargets() {
		return numTargets;
	}
}
