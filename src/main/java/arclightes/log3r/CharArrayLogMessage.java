package main.java.arclightes.log3r;

import java.nio.CharBuffer;

public interface CharArrayLogMessage extends LogMessage {
	@Override // Allows chained appending of LogMessages
	CharArrayLogMessage append(LogMessage msg);

	CharArrayLogMessage append(CharBlock block);
	CharArrayLogMessage append(char c);
	CharArrayLogMessage append(char[] srcArray);
	CharArrayLogMessage append(char[] srcArray, int srcPos, int length);
	CharArrayLogMessage append(CharBuffer srcBuffer);
	CharArrayLogMessage append(int i);
	CharArrayLogMessage append(long l);
	CharArrayLogMessage append(double d);
	CharArrayLogMessage append(double d, int precision);
	CharArrayLogMessage appendMillisecondTimestamp(long msTimestamp);
}
