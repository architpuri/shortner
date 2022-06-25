package com.architpuri.shortner.util;

import org.apache.commons.validator.routines.UrlValidator;

public class CommonUtils {
    public static Boolean isValidUrl(final String url) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }
}
