package main.java.arclightes.log3r;

import java.text.ParseException;

// Not Thread Safe
final class DoubleCharArrayBuffer {
	private static final int DEFAULT_PRECISION = Log3rSettings.getInstance().getFloatingPointPrecisionDefault();
	private static final char[] NaN_CHARS = Double.toString(Double.NaN).toCharArray();
	private static final char[] POS_INFINITY_CHARS = Double.toString(Double.POSITIVE_INFINITY).toCharArray();
	private static final char[] NEG_INFINITY_CHARS = Double.toString(Double.NEGATIVE_INFINITY).toCharArray();
	private static final char DECIMAL_POINT = '.';
	private final NumeralCharArrayBuffer wholeBuffer = new NumeralCharArrayBuffer();
	private final NumeralCharArrayBuffer fractionalBuffer = new NumeralCharArrayBuffer();

    DoubleCharArrayBuffer() {

	}

	void appendDouble(final double d) {
		appendDouble(d, DEFAULT_PRECISION);
	}

	void appendDouble(final double d, final int precision) {
		if (Double.isNaN(d)) {
			wholeBuffer.appendChars(NaN_CHARS);
			return;
		}

		if (Double.isInfinite(d)) {
			if (d > 0.0d) {
				wholeBuffer.appendChars(POS_INFINITY_CHARS);
			} else {
				wholeBuffer.appendChars(NEG_INFINITY_CHARS);
			}
			return;
		}

		if (d == 0.0d) {
			wholeBuffer.appendChar('0');
			if (precision >= 1) {
				fractionalBuffer.bulkAppendZeros(precision);
			}
			return;
		}

		appendNonInfiniteOrZeroDouble(d, precision);
	}

	void appendNonInfiniteOrZeroDouble(final double d, int precision) {
		final double absRaised;
		final NumeralCharArrayBuffer.IntegerSign sign;
		if (d < 0.0d) {
			absRaised = -1.0d * Log3rUtils.raiseToPowerOfTen(d, precision);
			sign = NumeralCharArrayBuffer.IntegerSign.NEGATIVE;
			if (d > -1.0d)
				wholeBuffer.appendChar('-'); // Ensures negative sign is added for d in (-1.0d, 0.0d)
		} else {
			absRaised = Log3rUtils.raiseToPowerOfTen(d, precision);
			sign = NumeralCharArrayBuffer.IntegerSign.POSITIVE;
		}

		long precisionMult = (long) absRaised;

		if (absRaised - precisionMult >= 0.5d) { ++precisionMult; }

		final long divisor = Log3rUtils.getLongPowerOfTen(precision);
		fractionalBuffer.appendLong(precisionMult % divisor);
		wholeBuffer.appendLong((precisionMult / divisor), sign);
	}

    int copyToDestArrayAndReset(final char[] destArray, int destPos) {
        final int startDestPos = destPos;

		if (wholeBuffer.getLength() > 0) {
			destPos += wholeBuffer.copyToDestArrayAndReset(destArray, destPos);
		}

		destArray[destPos++] = DECIMAL_POINT; // Always append decimal point even if there's nothing in fractional buffer

		if (fractionalBuffer.getLength() > 0) {
			destPos += fractionalBuffer.copyToDestArrayAndReset(destArray, destPos);
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
