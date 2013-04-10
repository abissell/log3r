package main.java.arclightes.log3r;

final class NumeralCharArrayBuffer implements BulkNumeralCharAppender {
	private static final int MAX_LENGTH = 64;

	private final char[] buffer;
	private final int capacity;
	private int length = 0;

    NumeralCharArrayBuffer() {
		this(MAX_LENGTH);
	}

	NumeralCharArrayBuffer(final int capacity) {
		if (capacity > MAX_LENGTH)
			throw new IllegalArgumentException("" + capacity);

		this.buffer = new char[capacity];
		this.capacity = capacity;
	}

	public final NumeralCharArrayBuffer appendChar(char c) {
		buffer[length++] = c;
		return this;
	}

	public final NumeralCharArrayBuffer appendChars(final char[] srcArray) {
		System.arraycopy(srcArray, 0, buffer, length, srcArray.length);
		length += srcArray.length;
		return this;
	}

	public final NumeralCharArrayBuffer appendChars(final char[] srcArray,
													final int srcPos,
													final int lengthOfCopy) {
		System.arraycopy(srcArray, srcPos, buffer, length, lengthOfCopy);
		length += lengthOfCopy;
		return this;
	}

	void bulkAppendZeros(final int numZeros) {
		System.arraycopy(CharConsts.ZEROS_SOURCE_ARRAY, 0, buffer, length, numZeros);
		length += numZeros;
	}

	public final int appendZeroPaddedNonNegativeInt(int i, final int minLength) {
		if (i == 0) {
			bulkAppendZeros(minLength);
			return minLength;
		}

		int idx = capacity - 1;
		do {
			buffer[idx--] = (char) ((i % 10) + '0');
			i /= 10;
		} while (i > 0);

		++idx;

		int numPaddedZeros = minLength - (capacity - idx);
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
	public final int appendInt(int i) {
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
			buffer[idx--] = (char) ((i % 10) + '0');
			i /= 10;
		} while (i > 0);

		++idx;
		return moveBackAppendedUnsignedDigitsToFront(idx, sign);
	}

	public final int appendLong(long el) {
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

	int appendLong(final long el, final IntegerSign sign) {
		if (el == 0) {
			buffer[length++] = '0';
			return 1;
		}

		return copyUnsignedDigits(el, sign);
	}

	int copyUnsignedDigits(long el, final IntegerSign sign) {
		int idx = capacity - 1;
		do {
			buffer[idx--] = (char) ((el % 10L) + '0');
			el /= 10L;
		} while (el > 0L);

		++idx;
		return moveBackAppendedUnsignedDigitsToFront(idx, sign);
	}

	int moveBackAppendedUnsignedDigitsToFront(final int idx, final IntegerSign sign) {
		final int copyLength = capacity - idx;
		if (sign == IntegerSign.POSITIVE) {
			System.arraycopy(buffer, idx, buffer, length, copyLength);
			length += copyLength;
			return copyLength;
		} else if (sign == IntegerSign.NEGATIVE) {
			System.arraycopy(buffer, idx, buffer, length+1, copyLength);
			buffer[length] = CharConsts.NEGATIVE_SIGN;
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

	public final int flush(final char[] destArray, final int destPos) {
		if (length <= 0) {
			return length = 0;
		}

		final int lengthOfCopy = length;
		System.arraycopy(buffer, 0, destArray, destPos, lengthOfCopy);
		length = 0;
		return lengthOfCopy;
	}

    public String toString() {
        return String.valueOf(buffer, 0, length);
    }
}
