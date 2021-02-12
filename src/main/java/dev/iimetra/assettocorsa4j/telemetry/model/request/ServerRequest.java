package dev.iimetra.assettocorsa4j.telemetry.model.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class ServerRequest implements Serializable {

    private int identifier = 1;
    private int version = 1;
    private final int operationId;

    public ServerRequest(Operation operation) {
        this.operationId = operation.getOperationId();
    }
}
