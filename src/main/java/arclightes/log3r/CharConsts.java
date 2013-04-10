package main.java.arclightes.log3r;

enum CharConsts {
	@SuppressWarnings("UnusedDeclaration")INSTANCE; // Enum singleton

	static final char[] NaN = Double.toString(Double.NaN).toCharArray();
	static final char[] POS_INFINITY = Double.toString(Double.POSITIVE_INFINITY).toCharArray();
	static final char[] INFINITY = POS_INFINITY;
	static final char[] NEG_INFINITY = Double.toString(Double.NEGATIVE_INFINITY).toCharArray();
	static final char DECIMAL_POINT = '.';
	static final char NEGATIVE_SIGN = '-';
	static final char[] ZEROS_SOURCE_ARRAY = new char[512];
	static {
		for (int i = 0; i < ZEROS_SOURCE_ARRAY.length; i++)
			ZEROS_SOURCE_ARRAY[i] = '0';
	}
}
