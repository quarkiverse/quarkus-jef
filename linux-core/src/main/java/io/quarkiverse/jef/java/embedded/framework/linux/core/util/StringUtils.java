
package io.quarkiverse.jef.java.embedded.framework.linux.core.util;

import java.nio.ByteBuffer;
import java.util.Arrays;

@SuppressWarnings({ "unused", "SameParameterValue" })
public class StringUtils {
    private final static int DEFAULT_BUFF_SIZE = parseIntProperty("jef.log.buffer-size", 16);

    public static String dump(ByteBuffer buffer) {
        return dump(DEFAULT_BUFF_SIZE, buffer);
    }

    public static String dump(byte[] buffer) {
        return dump(ByteBuffer.wrap(buffer));
    }

    public static String dump16(ByteBuffer buffer) {
        return dump(16, buffer);
    }

    public static String dump10(ByteBuffer buffer) {
        return dump(10, buffer);
    }

    public static String dump16(byte[] bytes) {
        return dump16(ByteBuffer.wrap(bytes));
    }

    public static String dump10(byte[] bytes) {
        return dump10(ByteBuffer.wrap(bytes));
    }

    private static int parseIntProperty(String s, int defaultValue) {
        try {
            return Integer.parseInt(System.getProperty(s));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static String dump(int alignment, ByteBuffer buffer) {
        int position = buffer.position();

        StringBuilder result = new StringBuilder();

        int size = buffer.capacity() - buffer.position();

        StringBuilder separator = new StringBuilder("---");
        StringBuilder index = new StringBuilder("| ");
        for (int i = 0; i < alignment; i++) {
            separator.append("---");
            index.append(String.format("%02X ", i));
        }
        index.append("|");

        result
                .append(separator)
                .append("\n")
                .append(index)
                .append("\n")
                .append(separator)
                .append("\n");

        for (int i = 0; i < size; i += alignment) {
            result.append("| ");
            int capacity = i + alignment > size ? size - i : alignment;
            int spaces = alignment - capacity;
            for (int j = 0; j < capacity; j++) {
                result.append(String.format("%02X ", buffer.get(i + j)));
            }
            result.append(repeat("   ", Math.max(0, spaces)));
            result.append("|\n");
        }
        result
                .append(separator)
                .append("\n");

        buffer.position(position);
        return result.toString();
    }

    public static String repeat(String s, int count) {
        byte[] value = s.getBytes();
        if (count < 0) {
            throw new IllegalArgumentException("count is negative: " + count);
        }
        if (count == 1) {
            return s;
        }
        final int len = value.length;
        if (len == 0 || count == 0) {
            return "";
        }
        if (len == 1) {
            final byte[] single = new byte[count];
            Arrays.fill(single, value[0]);
            return new String(single);
        }
        if (Integer.MAX_VALUE / count < len) {
            throw new OutOfMemoryError("Repeating " + len + " bytes String " + count +
                    " times will produce a String exceeding maximum size.");
        }
        final int limit = len * count;
        final byte[] multiple = new byte[limit];
        System.arraycopy(value, 0, multiple, 0, len);
        int copied = len;
        for (; copied < limit - copied; copied <<= 1) {
            System.arraycopy(multiple, 0, multiple, copied, copied);
        }
        System.arraycopy(multiple, 0, multiple, copied, limit - copied);
        return new String(multiple);
    }
}
