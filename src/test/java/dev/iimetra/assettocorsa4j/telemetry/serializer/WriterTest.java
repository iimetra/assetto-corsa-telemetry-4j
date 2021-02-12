package dev.iimetra.assettocorsa4j.telemetry.serializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WriterTest {

    private Writer writer;

    @BeforeEach
    void setUp() {
        writer = new Writer();
    }

    @Test
    void testWriteInt() {
        byte[] expected = {1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0};

        writer.allocate(12);
        writer.writeInt(1);
        writer.writeInt(2);
        writer.writeInt(3);

        byte[] actual = writer.toByteArray();

        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }
}