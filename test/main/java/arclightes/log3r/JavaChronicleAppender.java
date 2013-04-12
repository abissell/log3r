package main.java.arclightes.log3r;

import java.util.Arrays;

enum JavaChronicleAppender {
	@SuppressWarnings("UnusedDeclaration")INSTANCE; // Enum singleton

	private static final long MAX_VALUE_DIVIDE_5 = Long.MAX_VALUE / 5;

	JavaChronicleAppender getInstance() {
		return INSTANCE;
	}

	static int appendDouble(final double d, final char[] array, int idx) {
		final long val = Double.doubleToRawLongBits(d);
		final int sign = (int) (val >>> 63);
		final int exp = (int) ((val >>> 52) & 2047);
		long mantissa = val & ((1L << 52) - 1);

		final int startIdx = idx;

		if (sign != 0) {
			array[idx++] = '-';
		}

		if (exp == 0 && mantissa == 0) {
			array[idx++] = '0';
			return idx - startIdx;
		} else if (exp == 2047) {
			if (mantissa == 0) {
				idx += appendArray(CharConsts.INFINITY, array, idx);
			} else {
				idx += appendArray(CharConsts.NaN, array, idx);
			}
			return idx - startIdx;
		} else if (exp > 0) {
			mantissa += 1L << 52;
		}

		final int shift = (1023 + 52) - exp;
		if (shift > 0) {
			// integer and faction
			if (shift < 53) {
				final long intValue = mantissa >> shift;
				idx += appendNonNegativeLong(intValue, array, idx);
				mantissa -= intValue << shift;
				if (mantissa > 0) {
					array[idx++] = '.';
					mantissa <<= 1;
					mantissa++;
					int precision = shift + 1;
					long error = 1;

					long value = intValue;
					int decimalPlaces = 0;
					while (mantissa > error) {
						// times 5*2 = 10
						mantissa *= 5;
						error *= 5;
						precision--;
						final long num = (mantissa >> precision);
						value = value * 10 + num;
						array[idx++] = (char) ('0' + num);
						mantissa -= num << precision;

						final double parsedValue = asDouble(value, 0, sign != 0, ++decimalPlaces);
						if (parsedValue == d)
							break;
					}
				}
				return idx - startIdx;

			} else {
				// faction.
				array[idx++] = '0';
				array[idx++] = '.';
				mantissa <<= 6;
				mantissa += (1 << 5);
				int precision = shift + 6;

				long error = (1 << 5);

				long value = 0;
				int decimalPlaces = 0;
				while (mantissa > error) {
					while (mantissa > MAX_VALUE_DIVIDE_5) {
						mantissa >>>= 1;
						error = (error + 1) >>> 1;
						precision--;
					}
					// times 5*2 = 10
					mantissa *= 5;
					error *= 5;
					precision--;
					if (precision >= 64) {
						decimalPlaces++;
						array[idx++] = '0';
						continue;
					}
					final long num = (mantissa >>> precision);
					value = value * 10 + num;
					final char c = (char) ('0' + num);
					assert !(c < '0' || c > '9');
					array[idx++] = c;
					mantissa -= num << precision;
					final double parsedValue = asDouble(value, 0, sign != 0, ++decimalPlaces);
					if (parsedValue == d)
						break;
				}
				return idx - startIdx;
			}
		}

		// large number
		mantissa <<= 10;
		int precision = -10 - shift;
		int digits = 0;
		while ((precision > 53 || mantissa > Long.MAX_VALUE >> precision) && precision > 0) {
			digits++;
			precision--;
			long mod = mantissa % 5;
			mantissa /= 5;
			int modDiv = 1;
			while (mantissa < MAX_VALUE_DIVIDE_5 && precision > 1) {
				precision -= 1;
				mantissa <<= 1;
				modDiv <<= 1;
			}
			mantissa += modDiv * mod / 5;
		}
		final long val2 = precision > 0 ? mantissa << precision : mantissa >>> -precision;

		idx += appendNonNegativeLong(val2, array, idx);
		if (digits >= 2) {
			bulkAppendZeros(digits, array, idx);
			idx += digits;
		} else if (digits == 1) {
			array[idx++] = '0';
		}

		return idx - startIdx;
	}

	static int appendDouble(double d,
			                int precision,
					 		final char[] destArray,
							int destPos) {
		if (precision < 0) precision = 0;
		if (precision >= TENS.length) precision = TENS.length - 1;
		final long power10 = TENS[precision];
		final int startPos = destPos;
		if (d < 0) {
			d = -d;
			destArray[destPos++] = '-';
		}
		final double d2 = d * power10;
		if (d2 > Long.MAX_VALUE || d2 < Long.MIN_VALUE + 1) {
			return (destPos - startPos) + appendDouble(d, destArray, destPos);
		}

		long val = (long) (d2 + 0.5);
		while (precision > 0 && val % 10 == 0) {
			val /= 10;
			precision--;
		}
		if (precision > 0)
			destPos += appendDouble0(val, precision, destArray, destPos);
		else
			destPos += appendNonNegativeLong(val, destArray, destPos);

		return destPos - startPos;
	}

	private static int appendDouble0(final long num,
									  final int precision,
									  final char[] destArray,
									  int destPos) {
		// find the number of digits
		long power10 = Math.max(TENS[precision], power10(num));
		// starting from the end, write each digit
		final long decimalPoint = TENS[precision - 1];
		final int startPos = destPos;
		while (power10 > 0) {
			if (decimalPoint == power10)
				destArray[destPos++] = '.';
			// write the lowest digit.
			destArray[destPos++] = (char) (num / power10 % 10 + '0');
			// remove that digit.
			power10 /= 10;
		}

		return destPos - startPos;
	}

	static int appendLong(long num, final char[] destArray, int destPos) {
		if (num < 0L) {
			num *= -1L;
			destArray[destPos++] = '-';
			return 1 + appendNonNegativeLong(num, destArray, destPos);
		} else if (num > 0L) {
			return appendNonNegativeLong(num, destArray, destPos);
		} else {
			destArray[destPos] = '0';
			return 1;
		}
	}

	static int appendNonNegativeLong(final long num, final char[] destArray, int destPos) {
		// find the number of digits
		long power10 = power10(num);
		// starting from the end, write each digit
		final int startPos = destPos;
		while (power10 > 0) {
			// write the lowest digit.
			destArray[destPos++] = (char) (num / power10 % 10L + '0');
			// remove that digit.
			power10 /= 10L;
		}

		return destPos - startPos;
	}

	static int appendInt(int num, final char[] destArray, int destPos) {
		if (num < 0) {
			num *= -1;
			destArray[destPos++] = '-';
			return 1 + appendNonNegativeInt(num, power10int(num), destArray, destPos);
		} else if (num > 0) {
			return appendNonNegativeInt(num, power10int(num), destArray, destPos);
		} else {
			destArray[destPos] = '0';
			return 1;
		}
	}

	static int appendZeroPaddedNonNegativeInt(int num,
											  final int minLength,
											  final char[] destArray,
											  int destPos) {
		if (num == 0) {
			bulkAppendZeros(minLength, destArray, destPos);
			return minLength;
		}

		final int power10idx = Arrays.binarySearch(TENS_INTS, num);
		int numZerosToAdd;
		if (power10idx >= 0) { // Found multiple of 10
			numZerosToAdd = minLength - power10idx - 1;
		} else {
			numZerosToAdd = minLength - (-1*power10idx) + 1;
		}

		for (; numZerosToAdd > 0; --numZerosToAdd) {
			destArray[destPos++] = '0';
		}

		return appendNonNegativeInt(num, power10intFromIdx(power10idx), destArray, destPos);
	}

	static int appendNonNegativeInt(final int num,
									int power10,
									final char[] destArray,
									int destPos) {
		// find the number of digits
		// starting from the end, write each digit
		final int startPos = destPos;
		while (power10 > 0) {
			// write the lowest digit.
			destArray[destPos++] = (char) (num / power10 % 10 + '0');
			// remove that digit.
			power10 /= 10;
		}

		return destPos - startPos;
	}

	private static double asDouble(long value, int exp, final boolean negative, int decimalPlaces) {
		if (decimalPlaces > 0 && value < Long.MAX_VALUE / 2) {
			if (value < Long.MAX_VALUE / (1L << 32)) {
				exp -= 32;
				value <<= 32;
			}
			if (value < Long.MAX_VALUE / (1L << 16)) {
				exp -= 16;
				value <<= 16;
			}
			if (value < Long.MAX_VALUE / (1L << 8)) {
				exp -= 8;
				value <<= 8;
			}
			if (value < Long.MAX_VALUE / (1L << 4)) {
				exp -= 4;
				value <<= 4;
			}
			if (value < Long.MAX_VALUE / (1L << 2)) {
				exp -= 2;
				value <<= 2;
			}
			if (value < Long.MAX_VALUE / (1L << 1)) {
				exp -= 1;
				value <<= 1;
			}
		}
		for (; decimalPlaces > 0; decimalPlaces--) {
			exp--;
			long mod = value % 5;
			value /= 5;
			int modDiv = 1;
			if (value < Long.MAX_VALUE / (1L << 4)) {
				exp -= 4;
				value <<= 4;
				modDiv <<= 4;
			}
			if (value < Long.MAX_VALUE / (1L << 2)) {
				exp -= 2;
				value <<= 2;
				modDiv <<= 2;
			}
			if (value < Long.MAX_VALUE / (1L << 1)) {
				exp -= 1;
				value <<= 1;
				modDiv <<= 1;
			}
			value += modDiv * mod / 5;
		}
		final double d = Math.scalb((double) value, exp);
		return negative ? -d : d;
	}

	private static final long[] TENS = new long[19];
	static {
		TENS[0] = 1;
		for (int i = 1; i < TENS.length; i++)
			TENS[i] = TENS[i - 1] * 10;
	}

	private static long power10(final long l) {
		int idx = Arrays.binarySearch(TENS, l);
		return idx >= 0 ? TENS[idx] : TENS[~idx - 1];
	}

	private static final int[] TENS_INTS = new int[19];
	static {
		TENS_INTS[0] = 1;
		for (int i = 1; i < TENS_INTS.length; i++)
			TENS_INTS[i] = TENS_INTS[i - 1] * 10;
	}

	private static int power10int(final int i) {
		int idx = Arrays.binarySearch(TENS_INTS, i);
		return idx >= 0 ? TENS_INTS[idx] : TENS_INTS[~idx - 1];
	}

	private static int power10intFromIdx(final int idx) {
		return idx >= 0 ? TENS_INTS[idx] : TENS_INTS[~idx - 1];
	}

	private static int appendArray(final char[] srcArray, final char[] destArray, final int destPos) {
		System.arraycopy(srcArray, 0, destArray, destPos, srcArray.length);
		return srcArray.length;
	}

	private static void bulkAppendZeros(final int numZeros, final char[] destArray, final int destPos) {
		System.arraycopy(CharConsts.ZEROS_SOURCE_ARRAY, 0, destArray, destPos, numZeros);
	}
}
