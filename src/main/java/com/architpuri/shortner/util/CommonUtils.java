package com.architpuri.shortner.util;

import org.apache.commons.validator.routines.UrlValidator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class CommonUtils {

    public static Long getDefaultExpiry() {
        // 10 days
        return 864000L;
    }

    public static Boolean isValidUrl(final String url) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }

    public static Long getCurrentEpochTime() {
        return LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
    }

    public static String getApplicationUrl() {
        return "http://127.0.0.1:8080/";
    }
}
