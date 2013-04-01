import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public enum TestUtils {
	INSTANCE;

	private static final AtomicPsuedoRandom random = new AtomicPsuedoRandom((int) System.currentTimeMillis());
	private static final Random javaRandom = new Random((int) System.currentTimeMillis() + 100000);

	private TestUtils() {

	}

	public static final int randomInt(final int n) {
		return random.nextInt(n);
	}

	public static final double randomDouble() {
		return javaRandom.nextDouble();
	}

	// cf. JCIP 12.1.3
	private static final class AtomicPsuedoRandom {
		private final AtomicInteger seed;

		private AtomicPsuedoRandom(final int seed) {
			this.seed = new AtomicInteger(seed);
		}

		public final int nextInt(final int n) {
			while (true) {
				final int s = seed.get();
				final int nextSeed = xorShift(s);
				if (seed.compareAndSet(s, nextSeed)) {
					final int remainder = s % n;
					return remainder > 0 ? remainder : remainder + n;
				}
			}
		}
	}

	// cf. JCIP 12.1.3
	private static int xorShift(int y) {
		y ^= (y << 6);
		y ^= (y >>> 21);
		y ^= (y << 7);

		return y;
	}
}
