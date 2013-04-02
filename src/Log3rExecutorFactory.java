import java.util.concurrent.*;

enum Log3rExecutorFactory {
	@SuppressWarnings("UnusedDeclaration") INSTANCE; // Enum singleton

	public static final Executor CACHED_THREAD_EXECUTOR = Executors.newCachedThreadPool();
}
