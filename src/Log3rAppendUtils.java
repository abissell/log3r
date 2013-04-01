import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import org.apache.log4j.Logger;

import java.nio.CharBuffer;
import java.text.ParseException;
import java.util.*;

enum Log3rAppendUtils {
	@SuppressWarnings("UnusedDeclaration") INSTANCE; // Enum singleton

	private static final Logger log = Logger.getLogger(Log3rAppendUtils.class);

	private static final int CHAR_ZERO = 48;
	private static final char[] NaN_CHARS = ("NaN").toCharArray();
	private static final char[] POS_INFINITY_CHARS = ("PositiveInfinity").toCharArray();
	private static final char[] NEG_INFINITY_CHARS = ("NegativeInfinity").toCharArray();
	private static final char NEGATIVE_SIGN = '-';

	private static final int UNLIMITED_PRECISION = -1;
	private static final int MAX_PRECISION = 14;
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

//    public static double charBufferToDouble(final CharBuffer buffer, final int length) throws ParseException {
//        return charBufferToDouble(buffer, length, 0);
//    }
//
//    public static double charBufferToDouble(final CharBuffer buffer, final int length, int offset) throws ParseException {
//        int calcLength = length + offset;
//        if (length == 0)
//            return 0;
//        int pos = offset;
//        boolean negative = false;
//        if (buffer.get(pos) == '-')
//        {
//            negative = true;
//            pos++;
//        }
//        int idxOfPoint = pos;
//        for (int i = pos; i < calcLength; i++)
//        {
//            //log.info("Buffer at " + buffer.get(i));
//            if (buffer.get(i) == '.')
//            {
//                idxOfPoint = i;
//                break;
//            }
//        }
//        //log.info("Idx of point " + idxOfPoint);
//        if (idxOfPoint == pos) // no decimal point, treat this like an int
//            return (double)charBufferToInt(buffer, length, offset);
//
//        int whole = 0;
//        for (int i = pos; i < idxOfPoint; i++)
//        {
//            int pow = idxOfPoint-i-1;
//            int val = getInt(buffer.get(i));
//            //log.info("Pow " + pow + " i=" + i + " length=" + length + " val=" + val + " charVal=" + buffer.get(i) + " raised=" + raiseToPower(val, pow));
//            whole += raiseToPower(val, pow);
//        }
//
//        double decimal = 0.0;
//        for (int i = idxOfPoint+1; i < calcLength; i++)
//        {
//            int pow = Math.abs(idxOfPoint-i);
//            int val = getInt(buffer.get(i));
//            //log.info("Pow " + pow + " i=" + i + " length=" + length + " val=" + val + " charVal=" + buffer.get(i) + " raised=" + raiseToPower(val, pow));
//            decimal += divideByPower(val, pow);
//        }
//
//        double num = whole + decimal;
//        if (negative)
//            num *= -1.0;
//        return num;
//        //return MathUtils.round(num, numDecimalPlaces);
//    }

//    public static int charBufferToInt(final CharBuffer buffer, final int length) throws ParseException {
//        return charBufferToInt(buffer, length, 0);
//    }
//
//    public static int charBufferToInt(final CharBuffer buffer, final int length, final int offset) throws ParseException {
//        int calcLength = length + offset;
//        if (length == 0)
//            return 0;
//        int pos = offset;
//        boolean negative = false;
//        if (buffer.get(pos) == '-')
//        {
//            negative = true;
//            pos++;
//        }
//        int num = 0;
//        for (int i = pos; i < calcLength; i++)
//        {
//            int pow = calcLength-i-1;
//            int val = getInt(buffer.get(i));
//            //log.info("Pow " + pow + " i=" + i + " length=" + length + " val=" + val + " charVal=" + buffer.get(i) + " raised=" + raiseToPower(val, pow));
//            num += raiseToPower(val, pow);
//
//        }
//        if (negative)
//            num *= -1;
//        return num;
//    }
//
//    public static long charBufferToLong(final CharBuffer buffer, final int length) throws ParseException {
//        return charBufferToLong(buffer, length, 0);
//    }
//
//    public static long charBufferToLong(final CharBuffer buffer, final int length, final int offset) throws ParseException {
//        int calcLength = length + offset;
//        if (length == 0)
//            return 0;
//        int pos = offset;
//        boolean negative = false;
//        if (buffer.get(pos) == '-')
//        {
//            negative = true;
//            pos++;
//        }
//        long num = 0;
//        for (int i = pos; i < calcLength; i++)
//        {
//            int pow = calcLength-i-1;
//            int val = getInt(buffer.get(i));
//            //log.info("Pow " + pow + " i=" + i + " length=" + length + " val=" + val + " charVal=" + buffer.get(i) + " raised=" + raiseLongToPower(val, pow));
//            num += raiseLongToPower(val, pow);
//
//        }
//        if (negative)
//            num *= -1;
//        return num;
//    }

//    private static double divideByPower(double val, int power) throws ParseException {
//        switch (power)
//        {
//            case 0:
//                return val;
//            case 1:
//                return val/10.0;
//            case 2:
//                return val/100.0;
//            case 3:
//                return val/1000.0;
//            case 4:
//                return val/10000.0;
//            case 5:
//                return val/100000.0;
//            case 6:
//                return val/1000000.0;
//            case 7:
//                return val/10000000.0;
//            case 8:
//                return val/100000000.0;
//            case 9:
//                return val/1000000000.0;
//            case 10:
//                return val/10000000000.0;
//            case 11:
//                return val/10000000000.0;
//            case 12:
//                return val/100000000000.0;
//            case 13:
//                return val/1000000000000.0;
//            case 14:
//                return val/10000000000000.0;
//            case 15:
//                return val/100000000000000.0;
//            case 16:
//                return val/1000000000000000.0;
//            case 17:
//                return val/10000000000000000.0;
//            default:
//                throw new ParseException("Unhandled power of ten: " + power, 0);
//        }
//
//    }
//


	static final double raiseToPowerOfTen(final double val, final int power) throws ParseException {
		if (power > MAX_PRECISION)
			throw new ParseException("Unhandled power of ten: " + power, 0);

		return val * DOUBLE_POWERS_OF_TEN[power];
	}

    public static void charArrayToByteArray(final int length, final char[] charArr, final byte[] byteArr)  {

        for (int i = 0; i < length; i++) {
            byteArr[i] = (byte)charArr[i];
        }
    }

    public static int getInt(char i) {
        return i - CHAR_ZERO;
    }

    public static char getChar(int i) {
        return (char) (i + CHAR_ZERO);
    }

	public static void copyDoubleToCharArray(final DoubleCharArrayBuffer buffer, final double d, final int precision) {
		if (Double.isNaN(d)) {
			buffer.getWholeBuffer().appendChars(NaN_CHARS);
			return;
		}

		if (Double.isInfinite(d)) {
			if (d > 0) {
				buffer.getWholeBuffer().appendChars(POS_INFINITY_CHARS);
			} else {
				buffer.getWholeBuffer().appendChars(NEG_INFINITY_CHARS);
			}
			return;
		}

		int i = (int) d;
		buffer.getWholeBuffer().appendInt(i);



		if (i == 0) {
			buffer.getWholeBuffer().appendChar('0');
		}
	}

    public static void doubleToCharBuffer(final DoubleCharBuffer buffer, double d) throws SerializationException {
        doubleToCharBuffer(buffer, d, UNLIMITED_PRECISION);
    }

    public static void doubleToCharBuffer(final DoubleCharBuffer buffer, double d, int precision) throws SerializationException {
        try
        {
            int decimalPlace = 0;
            double tmp = d;
            while (Math.abs(Math.floor(tmp) - tmp) > 0.0000001)
            {
                decimalPlace++;
                tmp *= 10;
            }

            final char[] wholeBuf = buffer.getWholeBuffer();
            int i = (int)d;
            if (i == 0)
            {
                boolean negative = i < 0;
                int pos = wholeBuf.length - 1;
                if (negative)
                    wholeBuf[pos--] = '-';
                wholeBuf[pos] = '0';
                buffer.setWholeLength(wholeBuf.length - pos - 1);
            }
            else if (d <= -1 || d >= 1)
            {
                int pos = wholeBuf.length - 1;
                boolean negative = i < 0;
                i = Math.abs(i);
                while(i > 0) {
                    wholeBuf[pos--] = getChar(i % 10);
                    i /= 10;
                }
                if (negative)
                    wholeBuf[pos--] = '-';
                buffer.setWholeLength(wholeBuf.length - pos - 1);
            }
            if (decimalPlace > 0)
            {
                final char[] fractionalBuf = buffer.getFractionalBuffer();
                int pos = fractionalBuf.length - 1;
                long frac = Math.abs((long) tmp);

                for (int j= 0; j < decimalPlace; j++) {
                    final char c = getChar((int)(frac % 10));
                    fractionalBuf[pos--] = c;
                    frac /= 10;
                }
                buffer.setFractionalLength(fractionalBuf.length - pos - 1);
                buffer.setPrecision(precision);

            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw new SerializationException(e);
        }
    }
    
    public static void intToCharBuffer(final IntCharBuffer buffer, int i) throws SerializationException {

        try
        {
            if (! isValidNumber(i))
            {
                final char[] cBuf = buffer.getBuffer();
                cBuf[cBuf.length-1] = '0';
                buffer.setLength(1);
                return;
            }

            final char[] cBuf = buffer.getBuffer();
            if (i < 10 && i > 0)
            {
                cBuf[cBuf.length-1] = getChar(i);
                buffer.setLength(1);
                return;
            }
            int pos = cBuf.length - 1;
            boolean negative = i < 0;
            i = Math.abs(i);
            while(i > 0) {
                cBuf[pos--] = getChar(i % 10);
                i /= 10;
            }
            if (negative)
                cBuf[pos--] = '-';
            buffer.setLength(cBuf.length - pos - 1);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw new SerializationException(e);
        }

    }

	static final boolean isValidNumber(final double d) {
		return !Double.isNaN(d);

		// double negation used to check for NaN
		//noinspection DoubleNegation
		// return ! (d != d);
	}

    public static void longToCharBuffer(final LongCharBuffer buffer, long i) throws SerializationException {

        try
        {
            final char[] cBuf = buffer.getBuffer();
            if (i < 10)
            {
                cBuf[cBuf.length-1] = getChar((int)i);
                buffer.setLength(1);
                return;
            }
            int pos = cBuf.length - 1;
            boolean negative = i < 0;
            i = Math.abs(i);
            while(i > 0) {
                cBuf[pos--] = getChar((int)(i % 10));
                i /= 10L;
            }
            if (negative)
                cBuf[pos--] = '-';
            buffer.setLength(cBuf.length - pos - 1);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw new SerializationException(e);
        }

    }

    public static int dateToBuffer(final long timesamp, final Calendar c, final IntCharBuffer intCharBuffer, final CharBuffer output) throws SerializationException {
        c.setTimeInMillis(timesamp);
        intToCharBuffer(intCharBuffer, c.get(Calendar.YEAR));
        int length = intCharBuffer.copyToBufferAndReset(output);
        output.append('-');
        length++;
        intToCharBuffer(intCharBuffer, c.get(Calendar.MONTH)+1);
        length += intCharBuffer.copyToBufferAndReset(output);
        output.append('-');
        length++;
        intToCharBuffer(intCharBuffer, c.get(Calendar.DAY_OF_MONTH));
        length += intCharBuffer.copyToBufferAndReset(output);
        output.append(' ');
        length += 2;
        intToCharBuffer(intCharBuffer, c.get(Calendar.HOUR_OF_DAY));
        length += intCharBuffer.copyToBufferAndReset(output);
        output.append(':');
        length++;
        intToCharBuffer(intCharBuffer, c.get(Calendar.MINUTE));
        length += intCharBuffer.copyToBufferAndReset(output);
        output.append(':');
        length++;
        intToCharBuffer(intCharBuffer, c.get(Calendar.SECOND));
        length += intCharBuffer.copyToBufferAndReset(output);
        output.append('.');
        length++;
        intToCharBuffer(intCharBuffer, c.get(Calendar.MILLISECOND));
        length += intCharBuffer.copyToBufferAndReset(output);
        return length;
    }

	public static boolean isPowerOfTwo(final int positiveInt) {
		return Integer.bitCount(positiveInt) == 1;
	}
}
