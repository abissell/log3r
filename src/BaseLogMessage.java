// Not Thread Safe
abstract class BaseLogMessage implements LogMessage {
	char[] array;
	int msgLength;

	BaseLogMessage() {
		array = new char[SettingDefaults.getPerfLogMaxMsgLength()];
	}

	public final char[] array() {
		return array;
	}

	public final int msgLength() {
		return msgLength;
	}

	public void reset() {
		msgLength = 0;
	}

	@Override
	public final String toString() {
		return String.valueOf(array, 0, msgLength);
	}
}
