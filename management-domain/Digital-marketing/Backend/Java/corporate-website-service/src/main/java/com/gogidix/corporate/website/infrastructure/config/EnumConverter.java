package com.gogidix.corporate.website.infrastructure.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
@WritingConverter
public class EnumConverter implements Converter<Enum<?>, String> {

    @Override
    public String convert(Enum<?> source) {
        if (source == null) {
            return null;
        }
        return source.name();
    }

    public <T extends Enum<T>> T reverseConvert(String source, Class<T> enumType) {
        if (source == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumType, source);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
