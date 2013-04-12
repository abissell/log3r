package main.java.arclightes.log3r;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableDateTime;
import org.joda.time.chrono.GregorianChronology;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TestCachingVsNonCachingTimestamps extends AbstractBenchmark {
	private static final int TESTS_PER_ITER = 50000;
	private static final int NUM_ITER = 100;
	private static final long[] TEST_TIMES = new long[TESTS_PER_ITER];
	private static final long INTERVAL_LENGTH = 1000L * 60L * 60L * 3L;
	static {
		long timeMs = System.currentTimeMillis();
		timeMs = timeMs - INTERVAL_LENGTH;
		final long timePerTest = INTERVAL_LENGTH / TESTS_PER_ITER;
		// final long intervalToSelect = 1000L * 60L * 60L * 3L; // select random times from past 3 hours
		for (int i = 0; i < TESTS_PER_ITER; i++) {
			timeMs += timePerTest;
			TEST_TIMES[i] = timeMs;
		}
	}


	private static int cachedResults = 0;
	private static int nonCachedResults = 0;
	private static final char[] cachedDestArray = new char[100];
	private static final char[] nonCachedDestArray = new char[100];
	private static final TimestampCharAppender cachedAppender
			= new CachingTimestampCharAppender(new NumeralCharArrayBuffer());
	private static final TimestampCharAppender nonCachedAppender
			= new GregorianTimestampCharAppender(new NumeralCharArrayBuffer());

	public TestCachingVsNonCachingTimestamps() {

	}

	@BeforeClass
	public static void setup() {
		System.out.println("starting tests");
	}

	@Test
	public void runCachedTests() {
		for (int i = 0; i < NUM_ITER; i++) {
			try {
				runCachedTimes();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

	private void runCachedTimes() {
		for (int i = 0; i < TESTS_PER_ITER; i++) {
			cachedAppender.appendMillisecondTimestamp(TEST_TIMES[i], cachedDestArray, 0);
			cachedResults += cachedDestArray[0] += cachedDestArray[8];
		}
	}

	@Test
	public void runNonCachedTests() {
		for (int i = 0; i < NUM_ITER; i++) {
			try {
				runNonCachedTimes();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

	private void runNonCachedTimes() {
		for (int i = 0; i < TESTS_PER_ITER; i++) {
			nonCachedAppender.appendMillisecondTimestamp(TEST_TIMES[i], nonCachedDestArray, 0);
			nonCachedResults += nonCachedDestArray[0] += nonCachedDestArray[8];
		}
	}

	@AfterClass
	public static void printResultToPreventOptimization() {
		System.out.println("cachedResults=" + cachedResults + ", nonCachedResults=" + nonCachedResults);
	}
}
