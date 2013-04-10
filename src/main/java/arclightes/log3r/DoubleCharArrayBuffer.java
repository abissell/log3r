package main.java.arclightes.log3r;

// Not Thread Safe
final class DoubleCharArrayBuffer implements FlushableDoubleCharAppender {
	private static final int DEFAULT_PRECISION = Log3rDefaultContext.getInstance().getFloatingPointPrecisionDefault();
	private final NumeralCharArrayBuffer wholeBuffer;
	private final NumeralCharArrayBuffer fractionalBuffer;

    DoubleCharArrayBuffer() {
		wholeBuffer = new NumeralCharArrayBuffer();
		fractionalBuffer = new NumeralCharArrayBuffer(Log3rUtil.getMaximumPrecision());
	}

	public final void appendDouble(final double d) {
		appendDouble(d, DEFAULT_PRECISION);
	}

	public final void appendDouble(final double d, final int precision) {
		if (d != d) {
			wholeBuffer.appendChars(CharConsts.NaN);
			return;
		}

		if (Double.isInfinite(d)) {
			if (d > 0.0d) {
				wholeBuffer.appendChars(CharConsts.POS_INFINITY);
			} else {
				wholeBuffer.appendChars(CharConsts.NEG_INFINITY);
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

	private void appendNonInfiniteOrZeroDouble(final double d, int precision) {
		final double absRaised;
		final NumeralCharArrayBuffer.IntegerSign sign;
		if (d < 0.0d) {
			absRaised = -1.0d * Log3rUtil.raiseToPowerOfTen(d, precision);
			sign = NumeralCharArrayBuffer.IntegerSign.NEGATIVE;
			if (d > -1.0d)
				wholeBuffer.appendChar('-'); // Ensures negative sign is added for d in (-1.0d, 0.0d)
		} else {
			absRaised = Log3rUtil.raiseToPowerOfTen(d, precision);
			sign = NumeralCharArrayBuffer.IntegerSign.POSITIVE;
		}

		long precisionMult = (long) absRaised;

		if (absRaised - precisionMult >= 0.5d) { ++precisionMult; }

		final long divisor = Log3rUtil.getLongPowerOfTen(precision);
		fractionalBuffer.appendLong(precisionMult % divisor);
		wholeBuffer.appendLong((precisionMult / divisor), sign);
	}

    public final int flush(final char[] destArray, int destPos) {
        final int startDestPos = destPos;
		destPos += wholeBuffer.flush(destArray, destPos);
		destArray[destPos++] = CharConsts.DECIMAL_POINT; // Always append decimal point even if there's nothing in fractional buffer
		destPos += fractionalBuffer.flush(destArray, destPos);
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
