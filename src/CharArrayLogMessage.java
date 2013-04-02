import java.nio.CharBuffer;

public interface CharArrayLogMessage extends LogMessage {
	LogMessage append(CharBlock block);
	LogMessage append(char c);
	LogMessage append(char[] srcArray);
	LogMessage append(char[] srcArray, int from, int to);
	LogMessage append(CharBuffer srcBuffer);
	LogMessage append(int i);
	LogMessage append(long l);
	LogMessage append(double d);
	LogMessage append(double d, int precision);
	LogMessage appendMillisecondTimestamp(long msTimestamp);
}
