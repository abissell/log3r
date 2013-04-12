package main.java.arclightes.log3r;

import com.lmax.disruptor.util.Util;

import java.util.Calendar;

@SuppressWarnings("FinalStaticMethod")
enum Log3rUtil {
	@SuppressWarnings("UnusedDeclaration") INSTANCE; // Enum singleton

	private static final String DEFAULT_CONTEXT_NAME = "default";

	private static final int MAXIMUM_PRECISION = 16;
	private static final double[] DOUBLE_POWERS_OF_TEN = new double[MAXIMUM_PRECISION];
	static {
		DOUBLE_POWERS_OF_TEN[0]  = 1.0d;
		DOUBLE_POWERS_OF_TEN[1]  = 10.0d;
		DOUBLE_POWERS_OF_TEN[2]  = 100.0d;
		DOUBLE_POWERS_OF_TEN[3]  = 1000.0d;
		DOUBLE_POWERS_OF_TEN[4]  = 10000.0d;
		DOUBLE_POWERS_OF_TEN[5]  = 100000.0d;
		DOUBLE_POWERS_OF_TEN[6]  = 1000000.0d;
		DOUBLE_POWERS_OF_TEN[7]  = 10000000.0d;
		DOUBLE_POWERS_OF_TEN[8]  = 100000000.0d;
		DOUBLE_POWERS_OF_TEN[9]  = 1000000000.0d;
		DOUBLE_POWERS_OF_TEN[10] = 10000000000.0d;
		DOUBLE_POWERS_OF_TEN[11] = 100000000000.0d;
		DOUBLE_POWERS_OF_TEN[12] = 1000000000000.0d;
		DOUBLE_POWERS_OF_TEN[13] = 10000000000000.0d;
		DOUBLE_POWERS_OF_TEN[14] = 100000000000000.0d;
		DOUBLE_POWERS_OF_TEN[15] = 1000000000000000.0d;
	}
	private static final long[] LONG_POWERS_OF_TEN = new long[16];
	static {
		LONG_POWERS_OF_TEN[0]  = 1L;
		LONG_POWERS_OF_TEN[1]  = 10L;
		LONG_POWERS_OF_TEN[2]  = 100L;
		LONG_POWERS_OF_TEN[3]  = 1000L;
		LONG_POWERS_OF_TEN[4]  = 10000L;
		LONG_POWERS_OF_TEN[5]  = 100000L;
		LONG_POWERS_OF_TEN[6]  = 1000000L;
		LONG_POWERS_OF_TEN[7]  = 10000000L;
		LONG_POWERS_OF_TEN[8]  = 100000000L;
		LONG_POWERS_OF_TEN[9]  = 1000000000L;
		LONG_POWERS_OF_TEN[10] = 10000000000L;
		LONG_POWERS_OF_TEN[11] = 100000000000L;
		LONG_POWERS_OF_TEN[12] = 1000000000000L;
		LONG_POWERS_OF_TEN[13] = 10000000000000L;
		LONG_POWERS_OF_TEN[14] = 100000000000000L;
		LONG_POWERS_OF_TEN[15] = 1000000000000000L;
	}

	static final String getDefaultContextName() {
		return DEFAULT_CONTEXT_NAME;
	}

	static final double raiseToPowerOfTen(final double val, final int power) {
		return val * DOUBLE_POWERS_OF_TEN[power];
	}

	static final long getLongPowerOfTen(final int power) {
		return LONG_POWERS_OF_TEN[power];
	}

	static final int getMaximumPrecision() {
		return MAXIMUM_PRECISION;
	}

	static final void charArrayToByteArray(final int length,
										   final char[] charArr,
										   final byte[] byteArr)  {
		for (int i = 0; i < length; i++) {
			byteArr[i] = (byte)charArr[i];
		}
	}

	static final void charArrayToByteArray(final int length,
										   final char[] charArr,
										   final byte[] byteArr,
										   final int byteArrDest) {
		for (int i = 0, j = byteArrDest; i < length; i++, j++) {
			byteArr[j] = (byte)charArr[i];
		}
	}

	static void appendTimestamp(final Calendar c, final BulkNumeralCharAppender appender) {
		appendTimestamp(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH),
						c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND),
						c.get(Calendar.MILLISECOND), appender);
	}

	static void appendTimestamp(final int year, final int month, final int day, final int hourOfDay,
								final int minute, final int second, final int millisecond,
								final BulkNumeralCharAppender appender) {

		appender.appendInt(year);
		appender.appendChar('-');
		appender.appendZeroPaddedNonNegativeInt(month, 2);
		appender.appendChar('-');
		appender.appendZeroPaddedNonNegativeInt(day, 2);
		appender.appendChar(' ');
		appender.appendZeroPaddedNonNegativeInt(hourOfDay, 2);
		appender.appendChar(':');
		appender.appendZeroPaddedNonNegativeInt(minute, 2);
		appender.appendChar(':');
		appender.appendZeroPaddedNonNegativeInt(second, 2);
		appender.appendChar('.');
		appender.appendZeroPaddedNonNegativeInt(millisecond, 3);
	}

	static final boolean isPowerOfTwo(final int positiveInt) {
		return Integer.bitCount(positiveInt) == 1;
	}

	static final int ceilingNextPowerOfTwo(final int x) {
		return Util.ceilingNextPowerOfTwo(x);
	}
	
	// Extensible enum example function
//	public final <U extends Enum<U> & main.java.arclightes.log3r.CharBlock> void appendAllBlocks(Class<U> blockType) {
//		for (main.java.arclightes.log3r.CharBlock block : blockType.getEnumConstants()) {
//			append(block.buffer());
//		}
//	}
}
