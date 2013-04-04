package main.java.arclightes.log3r;

import com.lmax.disruptor.*;

import java.util.concurrent.Executor;

public interface LogContext {
	String contextName();
	int getFileLengthMb();
	int getMaxMessageLengthChars();
	int getInitialMsgDataLength();
	float getMsgDataLengthLoadFactor();
	int getMessagesQueueSize();
	int getFloatingPointPrecisionDefault();
	EventFactory<SequencedLogMessage> getLogMsgFactory();
	Executor getFileWriteExecutor();
	ClaimStrategy getLogMsgClaimStrategy();
	WaitStrategy getFileWriteWaitStrategy();
	EventHandler<SequencedLogMessage> getFileWriteEventHandler();
	LogTarget getDefaultLogTarget();
}