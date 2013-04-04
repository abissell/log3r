package main.java.arclightes.log3r;

public interface PaddedLogMessageType extends LogMessageType {
	long sumPaddingToPreventOptimization(int index);
}
