package main.java.arclightes.log3r;

import java.nio.CharBuffer;

// Thread Safe
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
	private final CharBuffer buffer;

    private BasicCharBlock(final String str) {
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
