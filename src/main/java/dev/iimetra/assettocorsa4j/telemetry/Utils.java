package dev.iimetra.assettocorsa4j.telemetry;

import java.lang.reflect.Field;

public final class Utils {

    public static final int CHARS_TO_READ = 50;
    public static final String CAR_COORDINATES_FIELD_NAME = "carCoordinates";
    public static final int CAR_COORDINATES_ARR_LENGTH = 3;
    public static final int DEFAULT_FLOAT_ARR_LENGTH = 4;

    private Utils() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class cannot be instantiated.");
    }

    public static <T> int payloadSizeInBytes(Class<T> klass) {
        int size = 0;
        Field[] declaredFields = klass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getType().isPrimitive()) {
                if (field.getType().equals(Integer.TYPE)) {
                    size += Integer.BYTES;
                } else if (field.getType().equals(Float.TYPE)) {
                    size += Float.BYTES;
                } else if (field.getType().equals(Boolean.TYPE)) {
                    size += 1;
                } else if (field.getType().equals(Character.TYPE)) {
                    size += Character.BYTES;
                }
            } else {
                if (field.getType().equals(String.class)) {
                    size += CHARS_TO_READ * Character.BYTES;
                } else if (field.getType().equals(float[].class)) {
                    int ARR_LENGTH = field.getName().equals(CAR_COORDINATES_FIELD_NAME) ? CAR_COORDINATES_ARR_LENGTH : DEFAULT_FLOAT_ARR_LENGTH;
                    size += ARR_LENGTH * Float.BYTES;
                }
            }
        }
        return size;
    }
}
