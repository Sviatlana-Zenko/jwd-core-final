package com.epam.jwd.core_final.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static String getFormattedDateAndTime(LocalDateTime dateTime) {
        String format = PropertyReaderUtil.populateApplicationProperties().getDateTimeFormat();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        return dateTime.format(formatter);
    }
}
