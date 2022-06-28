package com.architpuri.shortner.service;

import com.architpuri.shortner.helper.ShorteningHelper;
import com.architpuri.shortner.helper.UrlDetailsHelper;
import com.architpuri.shortner.model.UrlDetails;
import com.architpuri.shortner.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@Slf4j
public class ShortenerService {

    @Autowired
    private ShorteningHelper shorteningHelper;

    @Autowired
    private UrlDetailsHelper urlDetailsHelper;

    public ResponseEntity<String> fetchUrlEntry(final String inputUrl) {
        UrlDetails urlDetails = urlDetailsHelper.getUrlDetailsByAlias(inputUrl);
        if (urlDetails != null) {
            if (urlDetails.getRedirect()) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(urlDetails.getOriginalUrl()))
                        .build();
            }
            return new ResponseEntity<>(urlDetails.getOriginalUrl(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Record Not found", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> createUrlEntry(final String originalUrl, final String custom, final Boolean redirect,
                                                 final Long expiryMillisecond) {
        /*UrlDetails urlDetails = urlDetailsHelper.getUrlDetailsByOriginal(originalUrl);
        if (urlDetails != null) {
            return new ResponseEntity<>("This url is already mapped.", HttpStatus.CONFLICT);
        }*/
        String aliasUrl;
        if (StringUtils.isNotEmpty(custom)) {
            UrlDetails urlDetailsCustom = urlDetailsHelper.getUrlDetailsByAlias(custom);
            if (urlDetailsCustom != null) {
                return new ResponseEntity<>("Custom Url cannot be used as it is already being used",
                        HttpStatus.CONFLICT);
            }
            aliasUrl = custom;
        } else {
            aliasUrl = shorteningHelper.generateRandomShortUrl(originalUrl.length()/2);
        }
        Long currentEpochTime = CommonUtils.getCurrentEpochTime();
        UrlDetails savedUrlDetails = urlDetailsHelper.saveDetails(new UrlDetails(aliasUrl, originalUrl, redirect, currentEpochTime, currentEpochTime + expiryMillisecond));
        if (savedUrlDetails != null) {
            return new ResponseEntity<>("Custom url assigned - " + CommonUtils.getApplicationUrl() + aliasUrl + " for provided url "
                    + originalUrl, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Some error Occurred. Please Try Again.", HttpStatus.NOT_ACCEPTABLE);
    }

    public ResponseEntity<String> deleteUrlEntry(final String originalUrl) {
        UrlDetails urlDetails = urlDetailsHelper.getUrlDetailsByOriginal(originalUrl);
        if (urlDetails == null) {
            return new ResponseEntity<>("Record not found", HttpStatus.NOT_FOUND);
        }
        urlDetailsHelper.deleteDetails(urlDetails);
        return new ResponseEntity<>("Record Deleted", HttpStatus.OK);
    }

    /**
     * Scheduled every 5 mins.
     */
    @Scheduled(fixedRate = 300000)
    public void cleanup() {
        urlDetailsHelper.cleanExpiredRecords();
    }
}