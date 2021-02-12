package dev.iimetra.assettocorsa4j.telemetry;

import static dev.iimetra.assettocorsa4j.telemetry.Utils.payloadSizeInBytes;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.iimetra.assettocorsa4j.telemetry.model.response.CarTelemetry;
import dev.iimetra.assettocorsa4j.telemetry.model.response.HandshakeResponse;
import dev.iimetra.assettocorsa4j.telemetry.model.response.LapTelemetry;
import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void testFieldsSizeToBytes() {
        int payloadSizeInBytes = payloadSizeInBytes(Foo.class);

        assertEquals(108, payloadSizeInBytes);
    }

    @Test
    void testHandshakeResponseSize() {
        int payloadSizeInBytes = payloadSizeInBytes(HandshakeResponse.class);

        assertEquals(408, payloadSizeInBytes);
    }

    @Test
    void testCarTelemetrySize() {
        int payloadSizeInBytes = payloadSizeInBytes(CarTelemetry.class);

        assertEquals(328, payloadSizeInBytes);
    }

    @Test
    void testLapTelemetrySize() {
        int payloadSizeInBytes = payloadSizeInBytes(LapTelemetry.class);

        assertEquals(212, payloadSizeInBytes);
    }

    private static class Foo {
        int bar;
        float baz;
        String boo;
    }
}