package main.java.arclightes.log3r;

interface NumeralCharAppender {
	int appendInt(int i);
	int appendZeroPaddedNonNegativeInt(int i, final int minLength);
	int appendLong(long el);
}
