package main.java.arclightes.log3r;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableDateTime;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestJodaChronVsGregorianCalendar extends AbstractBenchmark {
	private static final int TESTS_PER_ITER = 50000;
	private static final int NUM_ITER = 100;
	private static final long[] RANDOM_TIMES = new long[TESTS_PER_ITER];
	static {
		final long currentTimeMs = System.currentTimeMillis();
		final long intervalToSelect = System.currentTimeMillis();
		// final long intervalToSelect = 1000L * 60L * 60L * 3L; // select random times from past 3 hours
		for (int i = 0; i < TESTS_PER_ITER; i++) {

			final long randomLong = TestUtils.randomLong(intervalToSelect);
			RANDOM_TIMES[i] = currentTimeMs - randomLong;
		}
	}
	private static int jodaResults = 0;
	private static int gregorianResults = 0;
	private static final Calendar c = new GregorianCalendar();
	private static final ReadWritableDateTime dateTime = new MutableDateTime(GregorianChronology.getInstance());

	public TestJodaChronVsGregorianCalendar() {

	}

	@BeforeClass
	public static void setup() {
		System.out.println("starting tests");
	}

	@Test
	public void runJodaTime() {
		for (int i = 0; i < NUM_ITER; i++) {
			try {
				runJodaTimes();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

	private void runJodaTimes() {
		for (int i = 0; i < TESTS_PER_ITER; i++) {
			dateTime.setMillis(RANDOM_TIMES[i]);
			final int year = dateTime.getYear();
			final int month = dateTime.getMonthOfYear();
			final int day = dateTime.getDayOfMonth();
			final int hour = dateTime.getHourOfDay();
			final int minute = dateTime.getMinuteOfHour();
			final int second = dateTime.getSecondOfMinute();
			final int millis = dateTime.getMillisOfSecond();
			jodaResults = jodaResults + year + month + day + hour + minute + second + millis;
		}
	}

	@Test
	public void runGregorianTime() {
		for (int i = 0; i < NUM_ITER; i++) {
			try {
				runGregorianTimes(c);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

	private void runGregorianTimes(final Calendar c) {
		for (int i = 0; i < TESTS_PER_ITER; i++) {
			c.setTimeInMillis(RANDOM_TIMES[i]);
			final int year = c.get(Calendar.YEAR);
			final int month = c.get(Calendar.MONTH) + 1;
			final int day = c.get(Calendar.DAY_OF_MONTH);
			final int hour = c.get(Calendar.HOUR_OF_DAY);
			final int minute = c.get(Calendar.MINUTE);
			final int second = c.get(Calendar.SECOND);
			final int millis = c.get(Calendar.MILLISECOND);
			gregorianResults = gregorianResults + year + month + day + hour + minute + second + millis;
		}
	}

	@AfterClass
	public static void printResultToPreventOptimization() {
		System.out.println("jodaResults=" + jodaResults + ", gregorianResults=" + gregorianResults);
	}
}
