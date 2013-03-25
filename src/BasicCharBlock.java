import net.jcip.annotations.ThreadSafe;

import java.nio.CharBuffer;

@ThreadSafe
public enum BasicCharBlock implements CharBlock {

    ATSYMBOL(" @ "),
    COMMA(", "),
    COLON(": "),
    DASH(" -- "),
    MIDMARKET(" <-> "),
    EQUALS_SIGN("="),
    NOT_EQUAL_TO(" != "),
    GREATER_THAN_OR_EQUAL_TO(" >= "),
    GREATER_THAN(" > "),
    LESS_THAN(" < "),
    TIMES(" * ");

	private final char[] array;
	// TODO: Wrap with unmodifiable decorator
	private final CharBuffer buffer;
	private final String str;

    private BasicCharBlock(final String str) {
        this.array = str.toCharArray();
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
