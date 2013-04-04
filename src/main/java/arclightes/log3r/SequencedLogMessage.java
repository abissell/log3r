package main.java.arclightes.log3r;

public interface SequencedLogMessage {
	void prepForWrite(long sequence, LogMessageType messageType);
	LogMessage m();
	SequencedLogMessage addLogTarget(LogTarget target);
	long getSequence();
	LogTarget[] getLogTargets();
	int getNumTargets();
}
