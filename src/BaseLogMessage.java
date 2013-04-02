// Not Thread Safe
abstract class BaseLogMessage implements LogMessage {
	char[] array;
	int msgLength;

	BaseLogMessage() {
		array = new char[Log3rSettings.getInstance().getLog3rMaxMessageLengthChars()];
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
