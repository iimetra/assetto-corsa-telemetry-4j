package dev.iimetra.assettocorsa4j.telemetry.model.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HandshakeResponse implements Serializable {

    private String carName;
    private String driverName;
    private int identifier;
    private int version;
    private String trackName;
    private String trackConfig;
}
