package com.gogidix.infrastructure.config.infrastructure.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

/**
 * MongoDB converters for Enums.
 */
public class EnumConverter {

    @ReadingConverter
    public static class StringToEnumConverter<T extends Enum<T>> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            return Enum.valueOf(enumType, source);
        }
    }

    @WritingConverter
    public static class EnumToStringConverter<T extends Enum<T>> implements Converter<T, String> {
        @Override
        public String convert(T source) {
            return source.name();
        }
    }
}
