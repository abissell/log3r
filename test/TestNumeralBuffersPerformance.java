import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestNumeralBuffersPerformance extends AbstractBenchmark {
	private static final int TESTS_PER_ITER = 500000;
	private static final int NUM_ITER = 1000;
	private static final int[] RANDOM_INTS = new int[TESTS_PER_ITER];
	private static int RESULT = 0;

	public TestNumeralBuffersPerformance() {

	}

	@BeforeClass
	public static void setup() {
		System.out.println("starting tests");
		fillRandomSignedInts();
	}

	private static void fillRandomSignedInts() {
		for (int i = 0; i < TESTS_PER_ITER; i++) {
			RANDOM_INTS[i] = getRandomInt(30);
		}
	}

	private static int getRandomInt(final int n) {
		int random = TestUtils.randomInt(n);
		if (random == n)
			return 0;
		else
			return random;
	}

	@Test
	public void testLessThanOrEqualZeroEval() {
		for (int i = 0; i < NUM_ITER; i++) {
			for (int j = 0; j < TESTS_PER_ITER; j++) {
				final boolean addToResult = RANDOM_INTS[j] <= 0;
				if (addToResult)
					RESULT++;
			}
		}
	}

	@Test
	public void testEqualZeroEval() {
		for (int i = 0; i < NUM_ITER; i++) {
			for (int j = 0; j < TESTS_PER_ITER; j++) {
				final boolean addToResult = RANDOM_INTS[j] == 0;
				if (addToResult)
					RESULT++;
			}
		}
	}

	@AfterClass
	public static void printResultToPreventOptimization() {
		System.out.println("tests finished, RESULT=" + RESULT);
	}
}
