enum Log3rSettings {
	@SuppressWarnings("UnusedDeclaration") INSTANCE; // Enum singleton

	// TODO: Set these by reference to an XML file or other client input
	private static final int LOG3R_FILE_LENGTH_MB = 250;
	private static final int LOG3R_MAX_MESSAGE_LENGTH_CHARS = 131072;
    private static final int LOG3R_MESSAGES_QUEUE_SIZE = 8192;
	private static final int FLOATING_POINT_PRECISION_DEFAULT = 10;

	static Log3rSettings getInstance() {
		return INSTANCE;
	}

    public final int getLog3rFileLengthMb() {
        return LOG3R_FILE_LENGTH_MB;
    }

    public final int getLog3rMaxMessageLengthChars() {
        return LOG3R_MAX_MESSAGE_LENGTH_CHARS;
    }

    public final int getLog3rMessagesQueueSize() {
        return LOG3R_MESSAGES_QUEUE_SIZE;
    }

	public final int getFloatingPointPrecisionDefault() {
		return FLOATING_POINT_PRECISION_DEFAULT;
	}
}
