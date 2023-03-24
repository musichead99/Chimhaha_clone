package net.chimhaha.clone.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanToYNConverter implements AttributeConverter<Boolean, Character> {
    @Override
    public Character convertToDatabaseColumn(Boolean attribute) {
        Character c = null;
        if(attribute) {
            c = 'Y';
        } else {
            c = 'N';
        }

        return c;
    }

    @Override
    public Boolean convertToEntityAttribute(Character dbData) {
        Boolean b = null;

        if(dbData == 'Y') {
            b = true;
        } else {
            b = false;
        }
        return b;
    }
}