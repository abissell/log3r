package main.java.arclightes.log3r;

import java.nio.CharBuffer;

public interface CharArrayMessage extends LogMessage {
	@Override // Allows chained appending of CharArrayLogMessages
	CharArrayMessage append(LogMessage msg);

	CharArrayMessage append(CharBlock block);
	CharArrayMessage append(char c);
	CharArrayMessage append(char[] srcArray);
	CharArrayMessage append(char[] srcArray, int srcPos, int length);
	CharArrayMessage append(CharBuffer srcBuffer);
	CharArrayMessage append(int i);
	CharArrayMessage append(long l);
	CharArrayMessage append(double d);
	CharArrayMessage append(double d, int precision);
	CharArrayMessage appendMillisecondTimestamp(long msTimestamp);
}
