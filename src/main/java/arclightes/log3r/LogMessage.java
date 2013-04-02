package main.java.arclightes.log3r;

public interface LogMessage {
	char[] array();
	int msgLength();
	LogMessage append(LogMessage msg);
	void reset();
}
