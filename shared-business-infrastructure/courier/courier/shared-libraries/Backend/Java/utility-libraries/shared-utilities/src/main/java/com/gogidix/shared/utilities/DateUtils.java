package com.gogidix.shared.utilities;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    
    public static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public static String formatToIso(LocalDateTime dateTime) {
        return dateTime.format(ISO_FORMATTER);
    }
    
    public static LocalDateTime parseFromIso(String isoString) {
        return LocalDateTime.parse(isoString, ISO_FORMATTER);
    }
    
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }
    
    public static LocalDateTime fromDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }
    
    public static boolean isExpired(LocalDateTime dateTime) {
        return dateTime.isBefore(LocalDateTime.now());
    }
}