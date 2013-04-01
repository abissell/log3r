import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.Random;

public class TestLog3rUtilsPerformance extends AbstractBenchmark {
	private static final int TESTS_PER_ITER = 5000;
	private static final int DECIMAL_TESTS_PER_ITER = 500;
	private static final int NUM_ITER = 1000;
	private static final Random RANDOM_LONG = new Random(System.currentTimeMillis());
	private static final int RANDOM_SIZE = Math.max(TESTS_PER_ITER, DECIMAL_TESTS_PER_ITER);
	private static final char[] RANDOM_CHARS = new char[TESTS_PER_ITER];
	private static final int[] RANDOM_INTS = new int[TESTS_PER_ITER];
	private static final int[] RANDOM_INTS_2 = new int[TESTS_PER_ITER];
	private static final double[] RANDOM_DOUBLES = new double[TESTS_PER_ITER];
	private static int RESULT_1 = 0;
	private static int RESULT_2 = 0;
	private static int RESULT_3 = 0;

	public TestLog3rUtilsPerformance() {

	}

	@BeforeClass
	public static void setup() {
		fillRandomChars();
		fillRandomInts();
		fillRandomDoubles();
		System.out.println("starting tests");
	}

	private static void fillRandomChars() {
		for (int i = 0; i < RANDOM_SIZE; i++) {
			int randomInt = getRandomInt(10);

			final String asString = Integer.toString(randomInt);
			final char c = asString.charAt(0);
			RANDOM_CHARS[i] = c;
		}
	}

	private static void fillRandomInts() {
		for (int i = 0; i < RANDOM_SIZE; i++) {
			RANDOM_INTS[i] = getRandomInt(14);
			RANDOM_INTS_2[i] = getRandomInt(10);
		}
	}

	private static void fillRandomDoubles() {
		for (int i = 0; i < RANDOM_SIZE; i++) {
			final long randomLong = RANDOM_LONG.nextLong();
			RANDOM_DOUBLES[i] = Double.longBitsToDouble(randomLong);
		}
	}

	private static int getRandomInt(final int n) {
		int random = TestUtils.randomInt(n);
		if (random == n)
			return 0;
		else
			return random;
	}

	// @Test
	public void testCharIntConversion() {
		for (int i = 0; i < NUM_ITER; i++) {
			for (int j = 0; j < TESTS_PER_ITER; j++) {
				final int convertedInt = Log3rAppendUtils.getInt(RANDOM_CHARS[j]);
				RESULT_1 += convertedInt;
			}
		}
	}

	// @Test
	public void testSwitchingCharIntConversion() {
		for (int i = 0; i < NUM_ITER; i++) {
			for (int j = 0; j < TESTS_PER_ITER; j++) {
				final int convertedInt = getIntSwitch(RANDOM_CHARS[j]);
				RESULT_1 += convertedInt;
			}
		}
	}

	private static int getIntSwitch(char i) {
		switch (i)
		{
			case '1':
				return 1;
			case '2':
				return 2;
			case '3':
				return 3;
			case '4':
				return 4;
			case '5':
				return 5;
			case '6':
				return 6;
			case '7':
				return 7;
			case '8':
				return 8;
			case '9':
				return 9;
			default:
				return 0;
		}
	}

	// @Test
	public void testIntCharConversion() {
		for (int i = 0; i < NUM_ITER; i++) {
			for (int j = 0; j < TESTS_PER_ITER; j++) {
				final char convertedChar = Log3rAppendUtils.getChar(RANDOM_INTS[j]);
				RESULT_1 += convertedChar;
			}
		}
	}

	// @Test
	public void testSwitchingIntCharConversion() {
		for (int i = 0; i < NUM_ITER; i++) {
			for (int j = 0; j < TESTS_PER_ITER; j++) {
				final char convertedChar = getCharSwitch(RANDOM_INTS[j]);
				RESULT_1 += convertedChar;
			}
		}
	}

	// @Test
	public void testDecimalPlaceCalcFloorEquality() {
		for (int i = 0; i < NUM_ITER; i++) {
			for (int j = 0; j < TESTS_PER_ITER; j++) {
				int decimalPlace = 0;
				double tmp = RANDOM_DOUBLES[j];
				while (Math.floor(tmp) != tmp)
				{
					decimalPlace++;
					tmp *= 10;
				}
				RESULT_1 += decimalPlace;
			}
		}
	}

	// @Test
	public void testDecimalPlaceCalcFloorRange() {
		for (int i = 0; i < NUM_ITER; i++) {
			for (int j = 0; j < TESTS_PER_ITER; j++) {
				int decimalPlace = 0;
				double tmp = RANDOM_DOUBLES[j];
				while (Math.abs(Math.floor(tmp) - tmp) > 0.0000000001)
				{
					decimalPlace++;
					tmp *= 10;
				}
				RESULT_1 += decimalPlace;
			}
		}
	}

	private static char getCharSwitch(int i) {
		switch (i)
		{
			case 1:
				return '1';
			case 2:
				return '2';
			case 3:
				return '3';
			case 4:
				return '4';
			case 5:
				return '5';
			case 6:
				return '6';
			case 7:
				return '7';
			case 8:
				return '8';
			case 9:
				return '9';
			default:
				return '0';
		}
	}

	@Test
	public void testVeryLongSwitchingRaiseToPowersOfTen() {
		try
		{
			for (int i = 0; i < NUM_ITER; i++) {
				for (int j = 0; j < TESTS_PER_ITER; j++) {
					final double d = RANDOM_DOUBLES[j];
					final double raised = veryLongRaiseToPowerOfTen(d, RANDOM_INTS[j]);
					final long cast = (long) raised;
					RESULT_3 += cast;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static double veryLongRaiseToPowerOfTen(double val, int power) throws ParseException {
		switch (power)
		{
			case 0:
				return val;
			case 1:
				return val*10;
			case 2:
				return val*100;
			case 3:
				return val*1000;
			case 4:
				return val*10000;
			case 5:
				return val*100000;
			case 6:
				return val*1000000;
			case 7:
				return val*10000000;
			case 8:
				return val*100000000;
			case 9:
				return val*1000000000;
			case 10:
				return val*10000000000L;
			case 11:
				return val*100000000000L;
			case 12:
				return val*1000000000000L;
			case 13:
				return val*10000000000000L;
			case 14:
				return val*100000000000000L;
			case 15:
				return val*1000000000000000L;
			case 16:
				return val*10000000000000000L;
			case 17:
				return val*100000000000000000L;
			case 18:
				return val*1000000000000000000L;
			default:
				throw new ParseException("Unhandled power of ten: " + power, 0);
		}

	}

	@Test
	public void testSwitchingRaiseToPowersOfTen() {
		try
		{
			for (int i = 0; i < NUM_ITER; i++) {
				for (int j = 0; j < TESTS_PER_ITER; j++) {
					final double d = RANDOM_DOUBLES[j];
					final double raised = Log3rAppendUtils.raiseToPowerOfTen(d, RANDOM_INTS[j]);
					final long cast = (long) raised;
					RESULT_1 += cast;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// @Test
	public void testCompactSwitchingRaiseToPowersOfTen() {
		try
		{
			for (int i = 0; i < NUM_ITER; i++) {
				for (int j = 0; j < TESTS_PER_ITER; j++) {
					final double d = RANDOM_DOUBLES[j];
					final double raised = raiseToPowerOfTenCompact(d, RANDOM_INTS[j]);
					final long cast = (long) raised;
					RESULT_2 += cast;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static double raiseToPowerOfTenCompact(double val, int power) throws ParseException {
		switch (power)
		{
			case 0:
				return val;
			case 1:
				return val*10;
			case 2:
				return val*100;
			case 3:
				return val*1000;
			case 4:
				return val*10000;
			case 5:
				return val*100000;
			case 6:
				return val*1000000;
			case 7:
				return val*10000000;
			case 8:
				return val*100000000;
			case 9:
				return val*1000000000;
			default:
				throw new ParseException("Unhandled power of ten: " + power, 0);
		}
	}

	@AfterClass
	public static void printResultToPreventOptimization() {
		System.out.println("tests finished, RESULT_1=" + RESULT_1 + ", RESULT_2=" + RESULT_2 + ", RESULT_3=" + RESULT_3);
	}
}
