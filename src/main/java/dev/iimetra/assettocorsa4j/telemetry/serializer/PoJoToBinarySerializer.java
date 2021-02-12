package dev.iimetra.assettocorsa4j.telemetry.serializer;

import static dev.iimetra.assettocorsa4j.telemetry.Utils.CAR_COORDINATES_ARR_LENGTH;
import static dev.iimetra.assettocorsa4j.telemetry.Utils.CAR_COORDINATES_FIELD_NAME;
import static dev.iimetra.assettocorsa4j.telemetry.Utils.CHARS_TO_READ;
import static dev.iimetra.assettocorsa4j.telemetry.Utils.DEFAULT_FLOAT_ARR_LENGTH;
import static dev.iimetra.assettocorsa4j.telemetry.Utils.payloadSizeInBytes;

import dev.iimetra.assettocorsa4j.telemetry.exception.LoggedException;
import java.io.Serializable;
import java.lang.reflect.Field;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PoJoToBinarySerializer {
    private final Writer writer;
    private final Reader reader;

    public <T extends Serializable> byte[] serialize(T model) {
        Field[] declaredFields = model.getClass().getDeclaredFields();
        writer.allocate(payloadSizeInBytes(model.getClass()));
        for (Field field : declaredFields) {
            if (field.getType().isPrimitive()) {
                if (field.getType().equals(Integer.TYPE)) {
                    field.setAccessible(true);
                    try {
                        writer.writeInt(field.getInt(model));
                    } catch (IllegalAccessException e) {
                        // this should never happen
                        throw new RuntimeException(String.format("Field %s is not accessible.", field.getName()), e);
                    }
                }
            }
        }
        return writer.toByteArray();
    }

    public <T extends Serializable> T deserialize(byte[] data, Class<T> klass) {
        Field[] declaredFields = klass.getDeclaredFields();
        try {
            klass.getConstructor().setAccessible(true);

            reader.fill(data);
            T newInstance = klass.newInstance();

            for (Field field : declaredFields) {
                field.setAccessible(true);
                if (field.getType().isPrimitive()) {
                    if (field.getType().equals(Integer.TYPE)) {
                        field.setInt(newInstance, reader.readInt());
                    } else if (field.getType().equals(Float.TYPE)) {
                        field.setFloat(newInstance, reader.readFloat());
                    } else if (field.getType().equals(Boolean.TYPE)) {
                        field.setBoolean(newInstance, reader.readBoolean());
                    } else if (field.getType().equals(Character.TYPE)) {
                        field.setChar(newInstance, reader.readChar());
                    }
                } else {
                    if (field.getType().equals(String.class)) {
                        field.set(newInstance, reader.readString(CHARS_TO_READ));
                    } else if (field.getType().equals(float[].class)) {
                        int readSize = field.getName().equals(CAR_COORDINATES_FIELD_NAME) ? CAR_COORDINATES_ARR_LENGTH : DEFAULT_FLOAT_ARR_LENGTH;
                        field.set(newInstance, reader.readFloats(readSize));
                    }
                }
            }

            return newInstance;
        } catch (NoSuchMethodException e) {
            throw new LoggedException(String.format("Class %s does not have no params constructor", klass.getName()), e);
        } catch (IllegalAccessException e) {
            // this should never happen
            throw new RuntimeException("Field is not accessible.", e);
        } catch (InstantiationException e) {
            throw new LoggedException(String.format("Is not able to instantiate %s", klass.getName()), e);
        }
    }
}
