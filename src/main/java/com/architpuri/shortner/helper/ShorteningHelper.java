package com.architpuri.shortner.helper;

import com.architpuri.shortner.model.UrlDetails;
import com.architpuri.shortner.repo.UrlDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ShorteningHelper {

    @Autowired
    private UrlDetailsHelper urlDetailsHelper;

    private static final int NUM_CHARS_SHORT_LINK = 7;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private Random random = new Random();

    public String generateRandomShortUrl() {
        char[] result = new char[NUM_CHARS_SHORT_LINK];
        while (true) {
            for (int i = 0; i < NUM_CHARS_SHORT_LINK; i++) {
                int randomIndex = random.nextInt(ALPHABET.length() - 1);
                result[i] = ALPHABET.charAt(randomIndex);
            }
            String shortLink = new String(result);
            if (isAliasPresent(shortLink)) {
                return generateRandomShortUrl();
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
