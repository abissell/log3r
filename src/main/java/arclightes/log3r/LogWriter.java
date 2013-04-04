package main.java.arclightes.log3r;

public interface LogWriter {
	void log(final LogMessage message);
	void log(final LogTarget logTarget, final LogMessage message);
}
