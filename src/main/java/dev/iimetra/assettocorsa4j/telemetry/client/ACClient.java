package dev.iimetra.assettocorsa4j.telemetry.client;

import static dev.iimetra.assettocorsa4j.telemetry.Utils.payloadSizeInBytes;
import static dev.iimetra.assettocorsa4j.telemetry.model.request.Operation.DISMISS;
import static dev.iimetra.assettocorsa4j.telemetry.model.request.Operation.HANDSHAKE;
import static dev.iimetra.assettocorsa4j.telemetry.model.request.Operation.SUBSCRIBE_SPOT;
import static dev.iimetra.assettocorsa4j.telemetry.model.request.Operation.SUBSCRIBE_UPDATE;

import dev.iimetra.assettocorsa4j.telemetry.exception.ConnectionException;
import dev.iimetra.assettocorsa4j.telemetry.model.request.Operation;
import dev.iimetra.assettocorsa4j.telemetry.model.request.ServerRequest;
import dev.iimetra.assettocorsa4j.telemetry.model.response.CarTelemetry;
import dev.iimetra.assettocorsa4j.telemetry.model.response.HandshakeResponse;
import dev.iimetra.assettocorsa4j.telemetry.model.response.LapTelemetry;
import dev.iimetra.assettocorsa4j.telemetry.serializer.PoJoToBinarySerializer;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class ACClient {

    public static final int DEFAULT_AC_PORT = 9996;

    private final PoJoToBinarySerializer serializer;
    private final DatagramSocket socket;
    private final String ipAddress;
    private final int port;

    private ACClient() throws IllegalAccessException {
        throw new IllegalAccessException("Use of() methods to instantiate a client.");
    }

    public static ACClient of(String ipAddress, int port, DatagramSocket socket, PoJoToBinarySerializer serializer) {
        return new ACClient(serializer, socket, ipAddress, port);
    }

    public static ACClient of(String ipAddress, DatagramSocket socket, PoJoToBinarySerializer serializer) {
        return new ACClient(serializer, socket, ipAddress, DEFAULT_AC_PORT);
    }

    public HandshakeResponse connect() {
        try {
            socket.connect(InetAddress.getByName(ipAddress), port);
            log.debug("Is connected: {}", socket.isConnected());

            sendOperationRequest(HANDSHAKE);

            HandshakeResponse handshakeResponse = receiveResponse(HandshakeResponse.class);

            log.debug("Received response from the server {}", handshakeResponse);

            return handshakeResponse;
        } catch (UnknownHostException e) {
            throw new ConnectionException(String.format("Unable to connect to ip %s and port %s", ipAddress, port), e);
        }
    }

    public void subscribeCarTelemetry() {
        sendOperationRequest(SUBSCRIBE_UPDATE);
    }

    public CarTelemetry getCarTelemetry() {
        return receiveResponse(CarTelemetry.class);
    }

    public void subscribeLapTelemetry() {
        sendOperationRequest(SUBSCRIBE_SPOT);
    }

    public LapTelemetry getLapTelemetry() {
        return receiveResponse(LapTelemetry.class);
    }

    public void disconnect() {
        sendOperationRequest(DISMISS);
    }

    private void sendOperationRequest(Operation operation) {
        try {
            ServerRequest request = new ServerRequest(operation);
            byte[] serializedRequest = serializer.serialize(request);
            DatagramPacket reqPacket = new DatagramPacket(serializedRequest, serializedRequest.length);
            socket.send(reqPacket);
            log.debug("Has sent request [{}] to the server {}:{}", reqPacket, ipAddress, port);
        } catch (IOException ioException) {
            throw new ConnectionException(String.format("Is not able to send a packet to %s:%s", ipAddress, port), ioException);
        }
    }

    private <T extends Serializable> T receiveResponse(Class<T> klass) {
        try {
            byte[] receiveBuff = new byte[payloadSizeInBytes(klass)];
            DatagramPacket respPacket = new DatagramPacket(receiveBuff, receiveBuff.length);
            socket.receive(respPacket);
            log.debug("Has received response [{}] from the server {}:{}", respPacket, ipAddress, port);
            return serializer.deserialize(respPacket.getData(), klass);
        } catch (IOException ioException) {
            throw new ConnectionException(String.format("Is not able to receive a packet from %s:%s", ipAddress, port), ioException);
        }
    }
}
