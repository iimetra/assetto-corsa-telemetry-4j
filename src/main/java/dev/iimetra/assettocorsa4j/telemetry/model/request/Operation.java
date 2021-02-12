package dev.iimetra.assettocorsa4j.telemetry.model.request;

import lombok.Getter;

public enum Operation {
    HANDSHAKE(0),
    SUBSCRIBE_UPDATE(1),
    SUBSCRIBE_SPOT(2),
    DISMISS(3);

    @Getter
    private final int operationId;
    
    Operation(int operationId) {
        this.operationId = operationId;
    }
}
