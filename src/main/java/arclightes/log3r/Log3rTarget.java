package main.java.arclightes.log3r;

import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public enum Log3rTarget implements LogTarget {
	DEFAULT("default");

	private static final Logger log = Logger.getLogger(Log3rTarget.class);

	private static final int MAX_FILE_LENGTH = Log3rSettings.getInstance().getLog3rFileLengthMb();

	/* Guarded by "fileLock" */ private OutputStream outputStream;
	private int logIdx;
	private int bytesWritten = 0;
	private final String logFile;
	private final Lock fileLock = new ReentrantLock();

	private Log3rTarget(final String logFile) {
		this.logFile = "perf.log.file" + logFile;
	}

	public final OutputStream getLogOutput() {
		if (outputStream == null)
		{
			createAndStartNewOutputStream();
		} else if (bytesWritten > MAX_FILE_LENGTH) {
			indexAndStartReplacementOutputStream();
		}
		else {
			throw new IllegalStateException("Found bytesWritten=" + bytesWritten + " > MAX_FILE_LENGTH=" + MAX_FILE_LENGTH
			                                + " with null outputStream, should never occur!");
		}

		return outputStream;
	}

	private void createAndStartNewOutputStream() {
		String indexedLogFile = logFile;
		if (indexedLogFile == null)
			throw new RuntimeException("Perf log file config key perf.log.file." + this.toString() + " not set");

		try {
			logIdx = getLogStartIdx() + 1;
			indexedLogFile = indexedLogFile + "." + logIdx;
			bytesWritten = 0;
			outputStream = new BufferedOutputStream(new FileOutputStream(indexedLogFile, true));
			log.info("Starting perf log with log file=" + logFile);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new IllegalArgumentException(e);
		}
	}

	private void indexAndStartReplacementOutputStream() {
		try {
			String indexedLogFile = logFile;
			final int newIdx = logIdx++;
			indexedLogFile = indexedLogFile + "." + newIdx;
			outputStream.flush();
			outputStream.close();
			outputStream = new BufferedOutputStream(new FileOutputStream(indexedLogFile, true));
			bytesWritten = 0;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private int getLogStartIdx() {
		final String path = Log3rFileIo.getDirFromPath(logFile);
		final String fileName = Log3rFileIo.getFileFromPath(logFile);
		return Log3rFileIo.getMaxFileIdx(path, fileName);
	}

	public final void lockLog() {
		fileLock.lock();
	}

	public final void unlockLog() {
		fileLock.unlock();
	}
}
