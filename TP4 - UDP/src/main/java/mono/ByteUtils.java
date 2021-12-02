package mono;

import java.nio.ByteBuffer;

interface ByteUtils {
    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    static byte[] longToBytes(long x) {
        buffer.clear();
        buffer.putLong(0, x);
        return buffer.array();
    }

    static long bytesToLong(byte[] bytes) {
        buffer.clear();
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getLong();
    }
}
