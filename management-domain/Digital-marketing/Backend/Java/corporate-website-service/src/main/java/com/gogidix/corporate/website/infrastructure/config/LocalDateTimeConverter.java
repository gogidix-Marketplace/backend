package com.gogidix.corporate.website.infrastructure.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@ReadingConverter
@WritingConverter
public class LocalDateTimeConverter implements Converter<LocalDateTime, Date> {

    @Override
    public Date convert(LocalDateTime source) {
        if (source == null) {
            return null;
        }
        return Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
    }

    public LocalDateTime reverseConvert(Date source) {
        if (source == null) {
            return null;
        }
        return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
