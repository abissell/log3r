import java.nio.CharBuffer;

public interface CharArrayLogMessage {
	LogMessage append(CharBlock block);
	LogMessage append(char c);
	LogMessage append(char[] srcArray);
	LogMessage append(char[] srcArray, int from, int to);
	LogMessage append(CharBuffer srcBuffer);
}
