import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.Sequencer;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLongArray;

public class TestUnsafeSequence {
	private static final long INITIAL_CURSOR_VALUE = -1L;
	private static final int NUM_THREADS = 4;
	private final Sequence cursor = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);
	private final int sequencesToTest = 100;
	private final AtomicLongArray sequencesSeen = new AtomicLongArray(sequencesToTest);

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

	class LatchedWorker implements Runnable {
		private final CountDownLatch startSignal;
		private final int threadId;

		LatchedWorker(CountDownLatch startSignal, int threadId) {
			this.startSignal = startSignal;
			this.threadId = threadId;
		}
		public void run() {
			try {
				startSignal.await();
				doWork();
			} catch (InterruptedException ex) {} // return;
		}

		void doWork() {
			long sequence = 0L;
			while (sequence < sequencesToTest) {
				sequence = cursor.incrementAndGet();
				System.out.print(sequence + "-" + threadId + ", ");
			}
		}
	}
}
