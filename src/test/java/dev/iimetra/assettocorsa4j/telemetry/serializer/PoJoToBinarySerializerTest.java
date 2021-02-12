package dev.iimetra.assettocorsa4j.telemetry.serializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Serializable;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PoJoToBinarySerializerTest {

    private PoJoToBinarySerializer serializer;

    @BeforeEach
    void setUp() {
        serializer = new PoJoToBinarySerializer(new Writer(), new Reader());
    }

    @Test
    void testSymmetry() {
        Foo foo = new Foo(42, 24);

        Foo backAndForthFoo = serializer.deserialize(serializer.serialize(foo), Foo.class);

        assertEquals(foo, backAndForthFoo);
    }

    @Test
    void testSerialize() {
        byte[] expected = {42, 0, 0, 0, 24, 0, 0, 0};
        Foo foo = new Foo(42, 24);

        byte[] serialized = serializer.serialize(foo);

        assertEquals(Arrays.toString(expected), Arrays.toString(serialized));
    }

    @Test
    void testDeserialize() {
        Foo expected = new Foo(42, 24);
        byte[] serialized = {42, 0, 0, 0, 24, 0, 0, 0};

        Foo deserialized = serializer.deserialize(serialized, Foo.class);

        assertEquals(expected, deserialized);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    private static class Foo implements Serializable {

        private int bar;
        private int buz;
    }
}