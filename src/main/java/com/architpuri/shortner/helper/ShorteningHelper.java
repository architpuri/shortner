package com.architpuri.shortner.helper;

import com.architpuri.shortner.model.UrlDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ShorteningHelper {

    @Autowired
    private UrlDetailsHelper urlDetailsHelper;

    private static final int NUM_CHARS_SHORT_LINK = 7;
    private static final String ALPHABET_KEY = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private Random random = new Random();

    public String generateRandomShortUrl(final Integer length) {
        Integer shorteningLength = length;
        if (length > 7) {
            shorteningLength = NUM_CHARS_SHORT_LINK;
        }
        char[] result = new char[shorteningLength];
        while (true) {
            for (int i = 0; i < shorteningLength; i++) {
                int randomIndex = random.nextInt(ALPHABET_KEY.length() - 1);
                result[i] = ALPHABET_KEY.charAt(randomIndex);
            }
            String shortLink = new String(result);
            if (isAliasPresent(shortLink)) {
                return generateRandomShortUrl(shorteningLength);
            }
            return shortLink;
        }
    }

    private Boolean isAliasPresent(final String recommendedUrl) {
        UrlDetails details = urlDetailsHelper.getUrlDetailsByAlias(recommendedUrl);
        if (details != null) {
            return true;
        }
        return false;
    }
}
