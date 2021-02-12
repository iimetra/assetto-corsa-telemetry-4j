package dev.iimetra.assettocorsa4j.telemetry.serializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReaderTest {

    private Reader reader;

    @BeforeEach
    void setUp() {
        reader = new Reader();
    }

    @Test
    void testReadChar() {
        reader.fill(new byte[]{97, 0});

        assertEquals('a', reader.readChar());
    }

    @Test
    void testReadString() {
        reader.fill(new byte[]{97, 0, 98, 0, 99, 0});

        String actual = reader.readString(3);

        assertEquals("abc", actual);
    }

    @Test
    void testReadInt() {
        reader.fill(new byte[]{1, 0, 0, 0});

        assertEquals(1, reader.readInt());
    }

    @Test
    void testReadInts() {
        reader.fill(new byte[]{1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0});

        assertEquals(1, reader.readInt());
        assertEquals(2, reader.readInt());
        assertEquals(3, reader.readInt());
    }

    @Test
    void testReadFloat() {
        reader.fill(new byte[]{102, 102, -122, 64});

        assertEquals(4.2f, reader.readFloat());
    }

    @Test
    void testReadFloats() {
        reader.fill(new byte[]{102, 102, -122, 64, 61, 10, -41, -66, 97, -78, 41, 66});

        assertEquals(4.2f, reader.readFloat());
        assertEquals(-0.42f, reader.readFloat());
        assertEquals(42.4242f, reader.readFloat());
    }

    @Test
    void testReadBooleanFalse() {
        reader.fill(new byte[]{0});

        assertFalse(reader.readBoolean());
    }

    @Test
    void testReadBooleanTrue() {
        reader.fill(new byte[]{1});

        assertTrue(reader.readBoolean());
    }
}