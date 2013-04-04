package main.java.arclightes.log3r;

import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.Sequencer;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLongArray;

public class TestUnsafeSequence {
	private static final int NUM_THREADS = 4;
	private static final AtomicLongArray sequencesSeen = new AtomicLongArray(110);
	static {
		for (int i = 0; i < sequencesSeen.length(); i++) {
			sequencesSeen.getAndSet(i, Sequencer.INITIAL_CURSOR_VALUE);
		}
	}
	private static final Sequence cursor = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);
	private static final int sequencesToTest = 100;

	public TestUnsafeSequence() {

	}

	@Test
	public void testUnsafeSequence() {
		final CountDownLatch startSignal = new CountDownLatch(1);

		for (int i = 0; i < NUM_THREADS; i++) {
			Thread cursorPrintThread = new Thread(new LatchedWorker(startSignal, i));
			cursorPrintThread.start();
		}

		startSignal.countDown();
	}

	private static class LatchedWorker implements Runnable {
		private final CountDownLatch startSignal;
		private final int threadId;
		private final StringBuilder buf = new StringBuilder();

		LatchedWorker(CountDownLatch startSignal, int threadId) {
			this.startSignal = startSignal;
			this.threadId = threadId;
		}
		public void run() {
			try {
				startSignal.await();
				doWork();
			} catch (InterruptedException ex) {
				// Do nothing
			} // return;
		}

		void doWork() {
			long sequence = 0L;
			buf.append("THREAD ").append(threadId).append(":");
			while (sequence < sequencesToTest) {
				sequence = cursor.incrementAndGet();
				buf.append(sequence).append(", ");
				final long priorSequence = sequencesSeen.getAndSet((int) sequence, sequence);
				if (priorSequence != Sequencer.INITIAL_CURSOR_VALUE)
					throw new RuntimeException("" + priorSequence);
			}
			System.out.println(buf);
			System.out.println("Exited work thread with sequence = " + sequence);
		}
	}
}
