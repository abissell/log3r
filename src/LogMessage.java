public interface LogMessage {
	char[] array();
	int msgLength();
	LogMessage append(LogMessage msg);
	void reset();
}
