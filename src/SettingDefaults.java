// Enum Singleton
enum SettingDefaults {
	@SuppressWarnings("UnusedDeclaration")
	INSTANCE;

	private static final int PERF_LOG_MAX_MSG_LENGTH = 131072;
    private static final int PERF_LOG_QUEUE_SIZE = 8192;
    private static final int PERF_LOG_FILE_LENGTH_MB = 50;
	private static final int FLOATING_POINT_MAX_PRECISION_DEFAULT = 10;

    public static int getPerfLogFileLengthMb() {
        return PERF_LOG_FILE_LENGTH_MB;
    }

    public static int getPerfLogMaxMsgLength() {
        return PERF_LOG_MAX_MSG_LENGTH;
    }

    public static int getPerfLogQueueSize() {
        return PERF_LOG_QUEUE_SIZE;
    }

	public static int getFloatingPointMaxPrecisionDefault() {
		return FLOATING_POINT_MAX_PRECISION_DEFAULT;
	}
}
