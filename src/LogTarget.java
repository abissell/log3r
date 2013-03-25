import java.io.OutputStream;

public interface LogTarget {
	OutputStream getLogOutput();
	void lockLog();
	void unlockLog();
}
