final class NumeralCharArrayBuffer {
	private static final int MAX_LENGTH = 128;
	private static final char[] ZEROS_SOURCE_ARRAY = new char[MAX_LENGTH];
	static {
		for (int i = 0; i < MAX_LENGTH; i++)
			ZEROS_SOURCE_ARRAY[i] = '0';
	}
	private static final char NEGATIVE_SIGN = '-';
	private final char[] buffer;
	private final int capacity;
    private int length = 0;

    NumeralCharArrayBuffer() {
		buffer = new char[MAX_LENGTH];
		capacity = MAX_LENGTH;
	}

	NumeralCharArrayBuffer(final int capacity) {
		if (capacity > MAX_LENGTH)
			throw new IllegalArgumentException("" + capacity);

		buffer = new char[capacity];
		this.capacity = capacity;
	}

    int getLength() {
        return length;
    }

	void appendChar(final char c) {
		buffer[length++] = c;
	}

	void appendChars(final char[] srcArray) {
		System.arraycopy(srcArray, 0, buffer, length, srcArray.length);
		length += srcArray.length;
	}

	void bulkAppendZeros(final int numZeros) {
		System.arraycopy(ZEROS_SOURCE_ARRAY, 0, buffer, length, numZeros);
		length += numZeros;
	}

	int appendZeroPaddedNonNegativeInt(int i, final int minLength) {
		if (i == 0) {
			bulkAppendZeros(minLength);
			return minLength;
		}

		int idx = capacity - 1;
		do {
			buffer[idx--] = Log3rUtils.getChar(i % 10);
			i /= 10;
		} while (i > 0);

		int numPaddedZeros = minLength - ((capacity - 1) - idx);
		if (numPaddedZeros >= 2) {
			bulkAppendZeros(numPaddedZeros);
			return (moveBackAppendedUnsignedDigitsToFront(idx, IntegerSign.POSITIVE) + numPaddedZeros);
		} else if (numPaddedZeros == 1) {
			appendChar('0');
			return (moveBackAppendedUnsignedDigitsToFront(idx, IntegerSign.POSITIVE) + 1);
		} else {
			return moveBackAppendedUnsignedDigitsToFront(idx, IntegerSign.POSITIVE);
		}
	}

	// Returns number of characters appended
	int appendInt(int i) {
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

	int copyUnsignedDigits(int i, final IntegerSign sign) {
		int idx = capacity - 1;
		do {
			buffer[idx--] = Log3rUtils.getChar(i % 10);
			i /= 10;
		} while (i > 0);

		return moveBackAppendedUnsignedDigitsToFront(idx, sign);
	}

	int appendLong(long el) {
		if (el == 0L) {
			buffer[length++] = '0';
			return 1;
		}

		if (el > 0L) {
			return copyUnsignedDigits(el, IntegerSign.POSITIVE);
		} else {
			el *= -1L;
			return copyUnsignedDigits(el, IntegerSign.NEGATIVE);
		}
	}

	int appendLong(long el, final IntegerSign sign) {
		if (el == 0) {
			buffer[length++] = '0';
			return 1;
		}

		return copyUnsignedDigits(el, sign);
	}

	int copyUnsignedDigits(long el, final IntegerSign sign) {
		int idx = capacity - 1;
		do {
			buffer[idx--] = Log3rUtils.getChar(el % 10L);
			el /= 10L;
		} while (el > 0L);

		return moveBackAppendedUnsignedDigitsToFront(idx, sign);
	}

	int moveBackAppendedUnsignedDigitsToFront(final int idx, final IntegerSign sign) {
		final int copyLength = (capacity - 1) - idx;
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

	enum IntegerSign {
		POSITIVE,
		NEGATIVE
	}

	// Returns total length of characters copied to destArray
	int copyToDestArrayAndReset(final char[] destArray, final int destPos) {
		final int lengthOfCopy = length;
		length = 0;
		if (lengthOfCopy <= 0)
			return 0;
		System.arraycopy(buffer, 0, destArray, destPos, lengthOfCopy);
		return lengthOfCopy;
	}

    public String toString() {
        return String.valueOf(buffer, 0, length);
    }
}
