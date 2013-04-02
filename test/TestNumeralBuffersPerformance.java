import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestNumeralBuffersPerformance extends AbstractBenchmark {
	private static final int TESTS_PER_ITER = 500000;
	private static final int NUM_ITER = 1000;
	private static final int[] randomInts = new int[TESTS_PER_ITER];
	private static final double[] randomDoubles = new double[TESTS_PER_ITER];
	private static long result = 0;
	private static long[] results = new long[10];
	private static int testIndex = -1;

	public TestNumeralBuffersPerformance() {
		testIndex++;
	}

	@BeforeClass
	public static void setup() {
		System.out.println("starting tests");
		fillRandomSignedInts();
		fillRandomDoubles();
	}

	private static void fillRandomSignedInts() {
		for (int i = 0; i < TESTS_PER_ITER; i++) {
			randomInts[i] = getRandomInt(11);
		}
	}

	private static int getRandomInt(final int n) {
		int random = TestUtils.randomInt(n);
		if (random == n)
			return 0;
		else
			return random;
	}

	private static void fillRandomDoubles() {
		for (int i = 0; i < TESTS_PER_ITER; i++) {
			randomDoubles[i] = TestUtils.randomDouble();
		}
	}

	public void testLessThanOrEqualZeroEval() {
		for (int i = 0; i < NUM_ITER; i++) {
			for (int j = 0; j < TESTS_PER_ITER; j++) {
				final boolean addToResult = randomInts[j] <= 0;
				if (addToResult)
					result++;
			}
		}
	}

	public void testEqualZeroEval() {
		for (int i = 0; i < NUM_ITER; i++) {
			for (int j = 0; j < TESTS_PER_ITER; j++) {
				final boolean addToResult = randomInts[j] == 0;
				if (addToResult)
					result++;
			}
		}
	}

	@Test
	public void testDoubleToDoublePowersOfTen() { // Confirmed faster than raising to long powers
		for (int i = 0; i < NUM_ITER; i++) {
			for (int j = 0; j < TESTS_PER_ITER; j++) {
				try {
					final double result = Log3rUtils.raiseToPowerOfTen(randomDoubles[j], randomInts[j]);
					results[testIndex] = results[testIndex] + Double.doubleToLongBits(result);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}

	@AfterClass
	public static void printResultToPreventOptimization() {
		System.out.println("tests finished, result=" + result);
		for (int i = 0; i <= testIndex; i++) {
			System.out.println("tests finished, result[" + i + "] = " + results[i]);
		}
	}
}
