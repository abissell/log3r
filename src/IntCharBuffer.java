import java.nio.CharBuffer;

final class IntCharBuffer  {
    private final char[] buffer = new char[50];
    private int length;

    public IntCharBuffer() {

	}

	public char[] getBuffer() {
        return buffer;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }
    
    public int copyToBufferAndReset(final CharBuffer buf) {
        buf.put(buffer, buffer.length-length, length);
        final int tmpLength = length;
        length = 0;
        return tmpLength;
    }
    
    public String toString() {
        return String.valueOf(buffer, buffer.length-length, length);
    }
}
