package dev.iimetra.assettocorsa4j.telemetry.exception;

public class ConnectionException extends LoggedException {
    public ConnectionException(String message, Exception cause) {
        super(message, cause);
    }
}
