package dev.iimetra.assettocorsa4j.telemetry.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggedException extends RuntimeException {

    public LoggedException(String message, Exception cause) {
        super(message, cause);
        log.error("Exception happened: {}, caused by: {}", message, cause.getMessage(), cause);
    }
}
