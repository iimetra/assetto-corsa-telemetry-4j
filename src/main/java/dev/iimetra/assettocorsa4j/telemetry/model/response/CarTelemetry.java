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
public class CarTelemetry implements Serializable {
    private int identifier;
    private int size;

    private float speedKmh;
    private float speedMph;
    private float speedMs;

    private boolean isAbsEnabled;
    private boolean isAbsInAction;
    private boolean isTcInAction;
    private boolean isTcEnabled;
    private boolean isInPit;
    private boolean isEngineLimiterOn;
    private char undocumentedTwoBytes;

    private float accGVertical;
    private float accGHorizontal;
    private float accGFrontal;

    private int lapTime;
    private int lastLap;
    private int bestLap;
    private int lapCount;

    private float gas;
    private float brake;
    private float clutch;
    private float engineRPM;
    private float steer;
    private int gear;
    private float cgHeight;

    private float[] wheelAngularSpeed;
    private float[] slipAngle;
    private float[] slipAngleContactPatch;
    private float[] slipRatio;
    private float[] tyreSlip;
    private float[] ndSlip;
    private float[] load;
    private float[] dy;
    private float[] mz;
    private float[] tyreDirtyLevel;

    private float[] camberRAD;
    private float[] tyreRadius;
    private float[] tyreLoadedRadius;
    private float[] suspensionHeight;
    private float carPositionNormalized;
    private float carSlope;
    private float[] carCoordinates;

}
