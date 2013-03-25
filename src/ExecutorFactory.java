import java.util.concurrent.*;

// Enum Singleton
enum ExecutorFactory {
	@SuppressWarnings("UnusedDeclaration")
	INSTANCE;

	public static final Executor CACHED_THREAD_EXECUTOR = Executors.newCachedThreadPool();
}
