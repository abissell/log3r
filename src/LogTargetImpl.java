import net.jcip.annotations.GuardedBy;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;

public enum LogTargetImpl implements LogTarget {
	DEFAULT("default"),
	CONDENSED("condensed");

	private static final Logger log = Logger.getLogger(LogTargetImpl.class);

	private static final int MAX_FILE_LENGTH = SettingDefaults.getPerfLogFileLengthMb();

	@GuardedBy("semaphore") private OutputStream outputStream;
	private int logIdx;
	private int bytesWritten = 0;
	private final String logFile;
	private final Semaphore semaphore = new Semaphore(1);

	private LogTargetImpl(final String logFile) {
		this.logFile = "perf.log.file" + logFile;
	}

	public final OutputStream getLogOutput() {
		if (outputStream == null)
		{
			String indexedLogFile = logFile;
			if (indexedLogFile == null)
				throw new RuntimeException("Perf log file config key perf.log.file." + this.toString() + " not set");
			try
			{
				logIdx = getLogStartIdx() + 1;
				indexedLogFile = indexedLogFile + "." + logIdx;
				bytesWritten = 0;
				outputStream = new BufferedOutputStream(new FileOutputStream(indexedLogFile, true));
				log.info("Starting perf log with log file=" + logFile);
			}
			catch (Exception e)
			{
				log.error(e.getMessage(), e);
				throw new IllegalArgumentException(e);
			}
		}
		else if (bytesWritten > MAX_FILE_LENGTH)
		{
			try
			{
				String indexedLogFile = logFile;
				final int newIdx = logIdx++;
				indexedLogFile = indexedLogFile + "." + newIdx;
				outputStream.flush();
				outputStream.close();
				outputStream = new BufferedOutputStream(new FileOutputStream(indexedLogFile, true));
				bytesWritten = 0;
			}
			catch (Exception e)
			{
				log.error(e.getMessage(), e);
			}
		}
		else {
			throw new IllegalStateException("Found bytesWritten=" + bytesWritten + " > MAX_FILE_LENGTH=" + MAX_FILE_LENGTH
			                                + " with null outputStream, should never occur!");
		}

		return outputStream;
	}

	private int getLogStartIdx() {
		final String path = FileIo.getDirFromPath(logFile);
		final String fileName = FileIo.getFileFromPath(logFile);
		return FileIo.getMaxFileIdx(path, fileName);
	}

	public final void lockLog() {
		try
		{
			semaphore.acquire();
		}
		catch (InterruptedException e)
		{
			log.error(e.getMessage(), e);
		}
	}

	public final void unlockLog() {
		if (semaphore.availablePermits() == 0)
			semaphore.release();
	}
}
