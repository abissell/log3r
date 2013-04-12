package main.java.arclightes.log3r;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableDateTime;
import org.joda.time.chrono.GregorianChronology;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestBufferVsChronNumeralAppend extends AbstractBenchmark {
	private static final int TESTS_PER_ITER = 5000;
	private static final int NUM_ITER = 100;

	private static final double[] RANDOM_DOUBLES = new double[TESTS_PER_ITER];
	static {
		for (int i = 0; i < TESTS_PER_ITER; i++) {
			RANDOM_DOUBLES[i] = TestUtils.randomDouble(2000000);
		}
	}

	private static final long[] RANDOM_LONGS = new long[TESTS_PER_ITER];
	static {
		for (int i = 0; i < TESTS_PER_ITER; i++) {
			RANDOM_LONGS[i] = TestUtils.randomLong(2000000L);
		}
	}

	private static final int[] RANDOM_INTS = new int[TESTS_PER_ITER];
	static {
		for (int i = 0; i < TESTS_PER_ITER; i++) {
			RANDOM_INTS[i] = TestUtils.randomInt(200000);
		}
	}

	private static final long[] RANDOM_TIMES = new long[TESTS_PER_ITER];
	static {
		final long currentTimeMs = System.currentTimeMillis();
		// final long intervalToSelect = System.currentTimeMillis();
		final long intervalToSelect = 1000L * 60L * 60L * 3L; // select random times from past 3 hours
		for (int i = 0; i < TESTS_PER_ITER; i++) {

			final long randomLong = TestUtils.randomLong(intervalToSelect);
			RANDOM_TIMES[i] = currentTimeMs - randomLong;
		}
	}

	private static final CharArrayMessage bufferMessage = new CharArrayLog3rMessage();
	private static final CharArrayMessage chronMessage = new CharArrayLog3rChronMessage();

	private static final Map<CharArrayMessage, int[]> lengthResults = new HashMap<>();
	private static final Map<CharArrayMessage, int[]> firstElementResults = new HashMap<>();
	static {
		lengthResults.put(bufferMessage, new int[TESTS_PER_ITER]);
		firstElementResults.put(bufferMessage, new int[TESTS_PER_ITER]);
		lengthResults.put(chronMessage, new int[TESTS_PER_ITER]);
		firstElementResults.put(chronMessage, new int[TESTS_PER_ITER]);
	}

	public TestBufferVsChronNumeralAppend() {

	}

	@BeforeClass
	public static void setup() {
		System.out.println("starting tests");
	}

	@Test
	public void runBufferAppends() {
		runAppends(bufferMessage, lengthResults.get(bufferMessage), firstElementResults.get(bufferMessage));
	}

	@Test
	public void runChronAppends() {
		runAppends(chronMessage, lengthResults.get(chronMessage), firstElementResults.get(chronMessage));
	}

	private void runAppends(final CharArrayMessage msg, final int[] lengthRes, final int[] firstRes) {
		for (int i = 0; i < NUM_ITER; i++) {
			for (int j = 0; j < TESTS_PER_ITER; j++) {
				// appendDouble(msg, j, lengthRes, firstRes);
				// appendLong(msg, j, lengthRes, firstRes);
				appendInt(msg, j, lengthRes, firstRes);
				// appendTime(msg, j, lengthRes, firstRes);
			}
		}
	}

	private void appendDouble(final CharArrayMessage msg, final int idx, final int[] lengthRes, final int[] firstRes) {
		msg.append(RANDOM_DOUBLES[idx]);
		lengthRes[idx] += msg.msgLength();
		firstRes[idx] += msg.array()[0];
		msg.reset();
	}

	private void appendLong(final CharArrayMessage msg, final int idx, final int[] lengthRes, final int[] firstRes) {
		msg.append(RANDOM_LONGS[idx]);
		lengthRes[idx] += msg.msgLength();
		firstRes[idx] += msg.array()[0];
		msg.reset();
	}

	private void appendInt(final CharArrayMessage msg, final int idx, final int[] lengthRes, final int[] firstRes) {
		msg.append(RANDOM_INTS[idx]);
		lengthRes[idx] += msg.msgLength();
		firstRes[idx] += msg.array()[0];
		msg.reset();
	}

	private void appendTime(final CharArrayMessage msg, final int idx, final int[] lengthRes, final int[] firstRes) {
		msg.append(RANDOM_TIMES[idx]);
		lengthRes[idx] += msg.msgLength();
		firstRes[idx] += msg.array()[0];
		msg.reset();
	}

	@AfterClass
	public static void printResultToPreventOptimization() {
		System.out.println("chron: length=" + sum(lengthResults.get(chronMessage))
				           + ", first=" + sum(firstElementResults.get(chronMessage)));
		System.out.println("buffer: length=" + sum(lengthResults.get(bufferMessage))
		                   + ", first=" + sum(firstElementResults.get(bufferMessage)));
	}

	private static int sum(final int[] intArray) {
		int sum = 0;
		for (int i : intArray) {
			sum += i;
		}

		return sum;
	}
}
