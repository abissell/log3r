final class NumeralCharArrayBuffer {
	private static final int MAX_LENGTH = 512;
	private static final char[] ZEROS_SOURCE_ARRAY = new char[MAX_LENGTH];
	static {
		for (int i = 0; i < MAX_LENGTH; i++)
			ZEROS_SOURCE_ARRAY[i] = '0';
	}
	private static final char NEGATIVE_SIGN = '-';
	private final char[] buffer = new char[MAX_LENGTH];
    private int length;

    public NumeralCharArrayBuffer() {

	}

	public char[] getBuffer() {
        return buffer;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

	public void appendChar(final char c) {
		buffer[length++] = c;
	}

	public void appendChars(final char[] srcArray) {
		System.arraycopy(srcArray, 0, buffer, length, srcArray.length);
		length += srcArray.length;
	}

	public void bulkAppendZeros(final int numZeros) {
		System.arraycopy(ZEROS_SOURCE_ARRAY, 0, buffer, length, numZeros);
		length += numZeros;
	}

	// Returns number of characters appended
	public int appendInt(int i) {
		if (i == 0) {
			buffer[length++] = '0';
			return 1;
		}

		if (i > 0) {
			return copyUnsignedDigits(i, IntegerSign.POSITIVE);
		} else {
			i *= -1;
			return copyUnsignedDigits(i, IntegerSign.NEGATIVE);
		}
	}

	private int copyUnsignedDigits(int i, IntegerSign sign) {
		int idx = MAX_LENGTH - 1;
		do {
			buffer[idx--] = Log3rAppendUtils.getChar(i % 10);
			i /= 10;
		} while (i > 0);

		final int copyLength = (MAX_LENGTH - 1) - idx;
		if (sign == IntegerSign.POSITIVE) {
			System.arraycopy(buffer, idx, buffer, length, copyLength);
			length += copyLength;
			return copyLength;
		} else if (sign == IntegerSign.NEGATIVE) {
			System.arraycopy(buffer, idx, buffer, length+1, copyLength);
			buffer[length] = NEGATIVE_SIGN;
			length += (copyLength + 1);
			return copyLength + 1;
		} else {
			throw new IllegalArgumentException("Bad integer sign " + sign);
		}
	}

	private enum IntegerSign {
		POSITIVE,
		NEGATIVE
	}

	// Returns total length of characters copied to destArray
	public int copyToDestArrayAndReset(final char[] destArray, final int destPos) {
		final int lengthOfCopy = length;
		length = 0;
		if (lengthOfCopy <= 0)
			return 0;
		System.arraycopy(buffer, 0, destArray, destPos, lengthOfCopy);
		return lengthOfCopy;
	}

	// Returns total length of characters copied to destArray
	public int copyToDestArrayAndReset(final char[] destArray, final int destPos, final int lengthLimit) {
		final int lengthOfCopy = Math.min(length, lengthLimit);
		length = 0;
		if (lengthOfCopy <= 0) {
			return 0;
		}
		System.arraycopy(buffer, 0, destArray, destPos, lengthOfCopy);
		return lengthOfCopy;
	}

    public String toString() {
        return String.valueOf(buffer, 0, length);
    }
}
