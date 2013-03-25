import net.jcip.annotations.ThreadSafe;

import java.nio.CharBuffer;

@ThreadSafe
public enum ExtendedCharBlock implements CharBlock {

	TEST("TEST: "),
    ERROR("ERROR: ");

	private final char[] array;
	private final CharBuffer buffer;
	private final String str;

	private ExtendedCharBlock(final String str) {
		this.array = str.toCharArray();
		// TODO: Wrap with unmodifiable decorator
		this.buffer = CharBuffer.wrap(this.array);
		this.str = str;
	}

	public final char[] array() {
		return array;
	}

	public final CharBuffer buffer() {
		return buffer;
	}

	public final String getStr() {
		return str;
	}
}
