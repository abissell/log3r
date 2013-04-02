package main.java.arclightes.log3r;

import com.lmax.disruptor.util.Util;

import java.text.ParseException;
import java.util.Arrays;

@SuppressWarnings("FinalStaticMethod")
enum Log3rUtils {
	@SuppressWarnings("UnusedDeclaration") INSTANCE; // Enum singleton

	private static final int CHAR_ZERO = 48;
	private static final long CHAR_ZERO_LONG = 48L;

	private static final double[] DOUBLE_POWERS_OF_TEN = new double[15];
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
	}
	private static final long[] LONG_POWERS_OF_TEN = new long[15];
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
	}

	static final double raiseToPowerOfTen(final double val, final int power) {
		return val * DOUBLE_POWERS_OF_TEN[power];
	}

	static final long getLongPowerOfTen(final int power) {
		return LONG_POWERS_OF_TEN[power];
	}

    public static int getInt(char i) {
        return i - CHAR_ZERO;
    }

    public static char getChar(int i) {
        return (char) (i + CHAR_ZERO);
    }

	public static char getChar(long el) {
		return (char) (el + CHAR_ZERO_LONG);
	}

	static final void charArrayToByteArray(final int length, final char[] charArr, final byte[] byteArr)  {
		for (int i = 0; i < length; i++) {
			byteArr[i] = (byte)charArr[i];
		}
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
