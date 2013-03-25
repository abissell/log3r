final class DoubleCharArrayBuffer {
	private static final char[] NaN_CHARS = Double.toString(Double.NaN).toCharArray();
	private static final char[] POS_INFINITY_CHARS = Double.toString(Double.POSITIVE_INFINITY).toCharArray();
	private static final char[] NEG_INFINITY_CHARS = Double.toString(Double.NEGATIVE_INFINITY).toCharArray();
	private static final char DECIMAL_POINT = '.';
	private static final double DEFAULT_MAX_PRECISION = SettingDefaults.getFloatingPointMaxPrecisionDefault();
	private final NumeralCharArrayBuffer wholeBuffer = new NumeralCharArrayBuffer();
	private final NumeralCharArrayBuffer fractionalBuffer = new NumeralCharArrayBuffer();

    public DoubleCharArrayBuffer() {

	}

	public NumeralCharArrayBuffer getWholeBuffer() {
		return wholeBuffer;
	}

    public NumeralCharArrayBuffer getFractionalBuffer() {
        return fractionalBuffer;
    }

    public int getWholeLength() {
        return wholeBuffer.getLength();
    }

    public void setWholeLength(final int wholeLength) {
        wholeBuffer.setLength(wholeLength);
    }

    public int getFractionalLength() {
        return fractionalBuffer.getLength();
    }

    public void setFractionalLength(final int fractionalLength) {
        fractionalBuffer.setLength(fractionalLength);
    }

	public void appendDouble(final double d, final int precision) {
		if (Double.isNaN(d)) {
			wholeBuffer.appendChars(NaN_CHARS);
			return;
		}

		if (Double.isInfinite(d)) {
			if (d > 0) {
				wholeBuffer.appendChars(POS_INFINITY_CHARS);
			} else {
				wholeBuffer.appendChars(NEG_INFINITY_CHARS);
			}
			return;
		}

		if (d == 0) {
			wholeBuffer.appendChar('0');
			if (precision >= 1) {
				fractionalBuffer.bulkAppendZeros(precision);
			}
		}

		long el = 0 ;

		// final long el =

		int i = (int) d;
		wholeBuffer.appendInt(i);

		if (i == 0) {
			wholeBuffer.appendChar('0');
		}
	}

    public int copyToDestArrayAndReset(final char[] destArray, int destPos, final int precision) {
        final int startDestPos = destPos;

		if (wholeBuffer.getLength() > 0) {
			destPos += wholeBuffer.copyToDestArrayAndReset(destArray, destPos);
		}

		destArray[destPos++] = DECIMAL_POINT; // Always append decimal point even if there's nothing in fractional buffer

		if (fractionalBuffer.getLength() > 0) {
			if (precision == 0) {
				// Reset the buffer without copying
				fractionalBuffer.setLength(0);
			} else {
				if (precision <= -1) {
					destPos += fractionalBuffer.copyToDestArrayAndReset(destArray, destPos);
				} else {
					destPos += fractionalBuffer.copyToDestArrayAndReset(destArray, destPos, precision);
				}
			}
		}

        return destPos - startDestPos;
    }

    @Override
	public String toString() {
        final StringBuilder buf = new StringBuilder();
		buf.append(wholeBuffer.toString());
		buf.append('.');
		buf.append(fractionalBuffer.toString());

		return buf.toString();
    }
}
