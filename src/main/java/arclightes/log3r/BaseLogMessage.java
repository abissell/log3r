package main.java.arclightes.log3r;

// Not Thread Safe
abstract class BaseLogMessage implements LogMessage {
	final char[] array;
	int msgLength;

	BaseLogMessage() {
		array = new char[Log3rDefaultContext.getInstance().getMaxMessageLengthChars()];
	}

	public final char[] array() {
		return array;
	}

	public final int msgLength() {
		return msgLength;
	}

	public final void reset() {
		msgLength = 0;
	}

	@Override
	public final String toString() {
		return String.valueOf(array, 0, msgLength);
	}
}
