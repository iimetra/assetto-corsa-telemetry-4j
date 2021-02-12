package dev.iimetra.assettocorsa4j.telemetry.serializer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Writer {
    private ByteBuffer byteBuffer;

    void allocate(int size) {
        byteBuffer = ByteBuffer.allocate(size);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    void writeInt(int val) {
        byteBuffer.putInt(val);
    }

    byte[] toByteArray() {
        return byteBuffer.array();
    }
}
