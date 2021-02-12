package dev.iimetra.assettocorsa4j.telemetry.serializer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Reader {

    private static final char MALFORMED_DATA_BEGINS_WITH_CHAR = '%';

    private ByteBuffer byteBuffer;

    void fill(byte[] data) {
        byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    String readString(int length) {
        char[] arr = new char[length];

        for (int i = 0; i < length; i++) {
            arr[i] = readChar();
        }
        int cutIndex = cutIndex(length, arr);
        return new String(arr, 0, cutIndex);
    }

    float[] readFloats(int size) {
        float[] arr = new float[size];

        for (int i = 0; i < size; i++) {
            arr[i] = readFloat();
        }
        return arr;
    }

    int readInt() {
        return byteBuffer.getInt();
    }

    char readChar() {
        return byteBuffer.getChar();
    }

    float readFloat() {
        return byteBuffer.getFloat();
    }

    boolean readBoolean() {
        return byteBuffer.get() != 0;
    }

    private int cutIndex(int length, char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if(MALFORMED_DATA_BEGINS_WITH_CHAR == arr[i]){
                length = i;
            }
        }
        return length;
    }
}
