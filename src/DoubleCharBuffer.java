import java.nio.CharBuffer;

final class DoubleCharBuffer  {
	private static final char DECIMAL_POINT = '.';
	private final char[] wholeBuffer = new char[50];
    private final char[] fractionalBuffer = new char[50];
    private int wholeLength;
    private int fractionalLength;
    private int precision;

    public DoubleCharBuffer() {

	}

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(final int precision) {
        this.precision = precision;
    }

    public char[] getFractionalBuffer() {
        return fractionalBuffer;
    }

    public char[] getWholeBuffer() {
        return wholeBuffer;
    }

    public int getWholeLength() {
        return wholeLength;
    }

    public void setWholeLength(final int wholeLength) {
        this.wholeLength = wholeLength;
    }

    public int getFractionalLength() {
        return fractionalLength;
    }

    public void setFractionalLength(final int fractionalLength) {
        this.fractionalLength = fractionalLength;
    }

    public int copyToBufferAndReset(final CharBuffer buf) {
        if (wholeBuffer.length > 0)
            buf.put(wholeBuffer, wholeBuffer.length-wholeLength, wholeLength);

        int flen = 0;
        int len = 0;
        if (fractionalLength > 0)
        {
            buf.put(DECIMAL_POINT);
            len = precision == -1 ? fractionalLength : precision;
            buf.put(fractionalBuffer, fractionalBuffer.length-fractionalLength, len);
            flen = 1;
        }
        final int tmpLength = wholeLength+len+flen;
        wholeLength = 0;
        fractionalLength = 0;
        precision = -1;
        return tmpLength;
    }

    @Override
	public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(wholeBuffer, wholeBuffer.length-wholeLength,wholeLength);
        if (fractionalLength > 0)
        {
            buf.append(DECIMAL_POINT);
            buf.append(fractionalBuffer, fractionalBuffer.length-fractionalLength, fractionalLength);
        }
        return buf.toString();
    }
}
