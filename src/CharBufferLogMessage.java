import java.nio.CharBuffer;

public interface CharBufferLogMessage extends CharArrayLogMessage {
	CharBuffer buffer();
	LogMessage appendMillisecondTimestamp(long msTime);
	LogMessage append(int i);
	LogMessage append(long l);
	LogMessage append(double d);
	LogMessage append(double d, int precision);
}
