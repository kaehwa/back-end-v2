package com.example.gaehwa2.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter
public class VectorConverter implements AttributeConverter<float[], String> {

    @Override
    public String convertToDatabaseColumn(float[] attribute) {
        if (attribute == null) return null;
        return "[" + Arrays.toString(attribute)
                .replaceAll("[\\[\\] ]", "") + "]";
    }

    @Override
    public float[] convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String[] parts = dbData.replaceAll("[\\[\\]]", "").split(",");
        float[] result = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Float.parseFloat(parts[i].trim());
        }
        return result;
    }

}

