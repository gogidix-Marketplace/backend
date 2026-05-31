package com.gogidix.infrastructure.config.infrastructure.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * MongoDB converters for LocalDateTime.
 */
public class LocalDateTimeConverter {

    @ReadingConverter
    public static class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
        @Override
        public LocalDateTime convert(Date source) {
            return source.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }
    }

    @WritingConverter
    public static class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
        @Override
        public Date convert(LocalDateTime source) {
            return Date.from(source.atZone(ZoneId.systemDefault())
                    .toInstant());
        }
    }
}
