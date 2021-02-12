package dev.iimetra.assettocorsa4j.telemetry.client;

import static dev.iimetra.assettocorsa4j.telemetry.model.request.Operation.DISMISS;
import static dev.iimetra.assettocorsa4j.telemetry.model.request.Operation.HANDSHAKE;
import static dev.iimetra.assettocorsa4j.telemetry.model.request.Operation.SUBSCRIBE_SPOT;
import static dev.iimetra.assettocorsa4j.telemetry.model.request.Operation.SUBSCRIBE_UPDATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.iimetra.assettocorsa4j.telemetry.model.request.ServerRequest;
import dev.iimetra.assettocorsa4j.telemetry.model.response.CarTelemetry;
import dev.iimetra.assettocorsa4j.telemetry.model.response.HandshakeResponse;
import dev.iimetra.assettocorsa4j.telemetry.model.response.LapTelemetry;
import dev.iimetra.assettocorsa4j.telemetry.serializer.PoJoToBinarySerializer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ACClientTest {

    private ACClient client;

    @Mock
    private DatagramSocket socket;
    @Mock
    private PoJoToBinarySerializer serializer;

    private MockedStatic<InetAddress> mockAddress = mockStatic(InetAddress.class);

    @BeforeEach
    void setUp() {
        client = ACClient.of("mock", socket, serializer);
        mockAddress.when(() -> InetAddress.getByName(anyString())).thenReturn(null);
    }

    @AfterEach
    void tearDown() {
        mockAddress.close();
    }

    @Test
    void testConnect() throws IOException {
        HandshakeResponse expectedResponse = new HandshakeResponse("car", "driver", 1, 1, "track", "config");

        doNothing().when(socket).connect(any(), anyInt());
        when(serializer.serialize(any())).thenReturn(new byte[]{1, 0, 0});
        when(serializer.deserialize(any(), any())).thenReturn(expectedResponse);

        HandshakeResponse actual = client.connect();

        assertEquals(expectedResponse, actual);

        verify(socket).connect(eq(InetAddress.getByName("mock")), eq(9996));
        verify(socket).send(any(DatagramPacket.class));
        verify(socket).receive(any(DatagramPacket.class));
        verify(serializer).deserialize(any(), any());

        ArgumentCaptor<ServerRequest> captor = ArgumentCaptor.forClass(ServerRequest.class);
        verify(serializer).serialize(captor.capture());

        ServerRequest request = captor.getValue();
        assertEquals(HANDSHAKE.getOperationId(), request.getOperationId());
    }

    @Test
    void testInitMethod() {
        assertNotNull(ACClient.of("mock", socket, serializer));
    }

    @Test
    void testSubscribeCarTelemetry() {
        when(serializer.serialize(any())).thenReturn(new byte[]{1, 0, 0});

        client.subscribeCarTelemetry();

        ArgumentCaptor<ServerRequest> captor = ArgumentCaptor.forClass(ServerRequest.class);
        verify(serializer).serialize(captor.capture());

        ServerRequest request = captor.getValue();
        assertEquals(SUBSCRIBE_UPDATE.getOperationId(), request.getOperationId());
    }

    @Test
    void testSubscribeLapTelemetry() {
        when(serializer.serialize(any())).thenReturn(new byte[]{1, 0, 0});

        client.subscribeLapTelemetry();

        ArgumentCaptor<ServerRequest> captor = ArgumentCaptor.forClass(ServerRequest.class);
        verify(serializer).serialize(captor.capture());

        ServerRequest request = captor.getValue();
        assertEquals(SUBSCRIBE_SPOT.getOperationId(), request.getOperationId());
    }

    @Test
    void testDisconnect() {
        when(serializer.serialize(any())).thenReturn(new byte[]{1, 0, 0});

        client.disconnect();

        ArgumentCaptor<ServerRequest> captor = ArgumentCaptor.forClass(ServerRequest.class);
        verify(serializer).serialize(captor.capture());

        ServerRequest request = captor.getValue();
        assertEquals(DISMISS.getOperationId(), request.getOperationId());
    }

    @Test
    void testGetCarTelemetry() throws IOException {
        CarTelemetry expectedResponse = new CarTelemetry();
        when(serializer.deserialize(any(), any())).thenReturn(expectedResponse);

        CarTelemetry actual = client.getCarTelemetry();

        assertEquals(expectedResponse, actual);

        verify(socket).receive(any(DatagramPacket.class));
        verify(serializer).deserialize(any(), any());
    }

    @Test
    void testGetLapTelemetry() throws IOException {
        LapTelemetry expectedResponse = new LapTelemetry();
        when(serializer.deserialize(any(), any())).thenReturn(expectedResponse);

        LapTelemetry actual = client.getLapTelemetry();

        assertEquals(expectedResponse, actual);

        verify(socket).receive(any(DatagramPacket.class));
        verify(serializer).deserialize(any(), any());
    }
}