package main.java.arclightes.log3r;

enum Log3rSettings {
	@SuppressWarnings("UnusedDeclaration") INSTANCE; // Enum singleton

	// TODO: Set these by reference to an XML file or other client input
	private static final int HARD_MAX_FILE_LENGTH_MB = 1750;

	private static final int LOG3R_FILE_LENGTH_MB = Math.min(250, HARD_MAX_FILE_LENGTH_MB); // !! Note: Must be <= 1750 to avoid risking overflow on bytes filesize
	private static final int LOG3R_MAX_MESSAGE_LENGTH_CHARS = 131072;
	private static final int LOG3R_INITIAL_MSGDATA_LENGTH = 1024;
	private static final float LOG3R_MSGDATA_LENGTH_LOAD_FACTOR = 1.25f;
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

	public final int getLog3rInitialMsgDataLength() {
		return LOG3R_INITIAL_MSGDATA_LENGTH;
	}

	public final float getLog3rMsgDataLengthLoadFactor() {
		return LOG3R_MSGDATA_LENGTH_LOAD_FACTOR;
	}

    public final int getLog3rMessagesQueueSize() {
        return LOG3R_MESSAGES_QUEUE_SIZE;
    }

	public final int getFloatingPointPrecisionDefault() {
		return FLOATING_POINT_PRECISION_DEFAULT;
	}
}
