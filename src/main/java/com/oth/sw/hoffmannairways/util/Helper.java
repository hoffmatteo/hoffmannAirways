package com.oth.sw.hoffmannairways.util;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("LanguageDetectionInspection")
public class Helper {

    public static String getFormattedDate(Date oldDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return format.format(oldDate);
    }
}
