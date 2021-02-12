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
public class LapTelemetry implements Serializable {

    private int carIdentifierNumber;
    private int lap;
    private String driverName;
    private String carName;
    private int time;
}
