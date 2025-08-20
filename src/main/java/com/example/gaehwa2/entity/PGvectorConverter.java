package com.example.gaehwa2.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.pgvector.PGvector;

@Converter(autoApply = true)
public class PGvectorConverter implements AttributeConverter<PGvector, String> {

    @Override
    public String convertToDatabaseColumn(PGvector attribute) {
        return (attribute == null) ? null : attribute.toString();
    }

    @Override
    public PGvector convertToEntityAttribute(String dbData) {
        try {
            return (dbData == null) ? null : new PGvector(dbData);
        } catch (Exception e) {
            throw new IllegalArgumentException("Vector 변환 실패: " + dbData, e);
        }
    }
}


