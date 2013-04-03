package main.java.arclightes.log3r;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestBinarySemaphoreVsReentrantLock extends AbstractBenchmark {
	private static final int TESTS_PER_ITER = 5000000;
	private static final int NUM_ITER = 10000;
	private static int semaphoreModifiedValue = 0;
	private static volatile int semaphoreModifications = 0;
	private static int lockModifiedValue = 0;
	private static volatile int lockModifications = 0;
	private static final Semaphore binarySemaphore = new Semaphore(1);
	private static final Lock mutex = new ReentrantLock();
	private static final ExecutorService exec = Executors.newFixedThreadPool(4);

	public TestBinarySemaphoreVsReentrantLock() {

	}

	@BeforeClass
	public static void setup() {
		System.out.println("starting tests");

		for (int i = 0; i < NUM_ITER; i++) {
			try {
				runModifier(LockingType.BINARY_SEMAPHORE);
				runModifier(LockingType.REENTRANT_LOCK);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	@Test
	public void runLockModifier() {
		lockModifications = 0;

		final long startTime = System.nanoTime();

		for (int i = 0; i < NUM_ITER; i++) {
			try {
				runModifier(LockingType.REENTRANT_LOCK);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}

		// System.out.println("Lock modifications took " + (System.nanoTime() - startTime));
	}

	@Test
	public void runSemaphoreModifier() {
		semaphoreModifications = 0;

		final long startTime = System.nanoTime();

		for (int i = 0; i < NUM_ITER; i++) {
			try {
				runModifier(LockingType.BINARY_SEMAPHORE);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}

		// System.out.println("Semaphore modifications took " + (System.nanoTime() - startTime));
	}

	private static void runModifier(final LockingType type) throws InterruptedException {
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch finishLatch = new CountDownLatch(4);
		for (int i = 0; i < 4; i++) {
			if (type == LockingType.BINARY_SEMAPHORE)
				exec.submit(new BinarySemaphoreRunnableTask(startLatch, finishLatch));
			else
				exec.submit(new LockRunnableTask(startLatch, finishLatch));
		}
		startLatch.countDown();
		finishLatch.await();
	}

	static class BinarySemaphoreRunnableTask implements Runnable {
		private final CountDownLatch startLatch;
		private final CountDownLatch finishLatch;

		public BinarySemaphoreRunnableTask(final CountDownLatch startLatch, final CountDownLatch finishLatch) {
			this.startLatch = startLatch;
			this.finishLatch = finishLatch;
		}

		@Override
		public void run() {
			try {
				startLatch.await();

				while (semaphoreModifications < TESTS_PER_ITER) {
					// System.out.println("Making semaphore modification");

					binarySemaphore.acquire();
					try {
						if (semaphoreModifiedValue == 1)
							semaphoreModifiedValue = 0;
						else if (semaphoreModifiedValue == 0)
							semaphoreModifiedValue = 1;
						else
							throw new RuntimeException("Saw bad value " + semaphoreModifiedValue);

						++semaphoreModifications;
					} finally {
						if (binarySemaphore.availablePermits() == 0)
							binarySemaphore.release();
					}
				}

			} catch (Exception e) {

			} finally {
				finishLatch.countDown();
			}
		}
	}

	static class LockRunnableTask implements Runnable {
		private final CountDownLatch startLatch;
		private final CountDownLatch finishLatch;

		public LockRunnableTask(final CountDownLatch startLatch, final CountDownLatch finishLatch) {
			this.startLatch = startLatch;
			this.finishLatch = finishLatch;
		}

		@Override
		public void run() {
			try {
				startLatch.await();

				while (lockModifications < TESTS_PER_ITER) {

					mutex.lock();
					try {
						if (lockModifiedValue == 1)
							lockModifiedValue = 0;
						else if (lockModifiedValue == 0)
							lockModifiedValue = 1;
						else
							throw new RuntimeException("Saw bad value " + lockModifiedValue);

						++lockModifications;
					} finally {
						mutex.unlock();
					}
				}
			} catch (Exception e) {

			} finally {
				finishLatch.countDown();
			}
		}
	}

	private enum LockingType {
		BINARY_SEMAPHORE,
		REENTRANT_LOCK
	}

	@AfterClass
	public static void printResultToPreventOptimization() {
		System.out.println("tests finished, binarySemaphoreResult=" + semaphoreModifiedValue + ", binarySemaphoreModifications=" + semaphoreModifications +
				           ", lockResult=" + lockModifiedValue + ", lockModifications=" + lockModifications);
	}
}
