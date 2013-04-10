package main.java.arclightes.log3r;

// Not Thread Safe
public final class PaddedCharArrayLog3rMessage extends CharArrayLog3rMessage {
	public volatile long p1, p2, p3, p4, p5, p6 = 7L;

	/**
	 * Default constructor
	 */
	public PaddedCharArrayLog3rMessage()
	{
		super();
	}

	public final long sumPaddingToPreventOptimisation()
	{
		return p1 + p2 + p3 + p4 + p5 + p6;
	}
}

