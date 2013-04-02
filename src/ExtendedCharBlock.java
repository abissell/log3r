import java.nio.CharBuffer;

// Thread Safe
public enum ExtendedCharBlock implements CharBlock {

	TEST("TEST: "),
    ERROR("ERROR: ");

	private final char[] array;
	private final CharBuffer buffer;

	private ExtendedCharBlock(final String str) {
		this.array = str.toCharArray();
		final CharBuffer wrapBuffer = CharBuffer.wrap(this.array);
		this.buffer = wrapBuffer.asReadOnlyBuffer();
	}

	public final char[] array() {
		return array;
	}

	public final CharBuffer buffer() {
		return buffer;
	}
}
