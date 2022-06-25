package com.architpuri.shortner.service;

import com.architpuri.shortner.ShorteningUtil;
import com.architpuri.shortner.model.UrlDetails;
import com.architpuri.shortner.repo.UrlDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@Slf4j
public class ShortenerService {

    private String APP_URL = "http://127.0.0.1:8080/";

    @Autowired
    private UrlDetailsRepository urlDetailsRepository;

    @Autowired
    private ShorteningUtil shorteningUtil;

    public ResponseEntity<String> fetchUrlEntry(final String inputUrl) {
        UrlDetails urlDetails = urlDetailsRepository.findByAliasUrl(inputUrl);
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

    public ResponseEntity<String> createUrlEntry(final String inputUrl, final String custom, final Boolean redirect) {
        UrlDetails urlDetails = urlDetailsRepository.findByOriginalUrl(inputUrl);
        if (urlDetails != null) {
            return new ResponseEntity<>("This url is already mapped.", HttpStatus.CONFLICT);
        }
        String aliasUrl;
        if (StringUtils.isNotEmpty(custom)) {
            UrlDetails urlDetailsCustom = urlDetailsRepository.findByAliasUrl(custom);
            if (urlDetailsCustom != null) {
                return new ResponseEntity<>("Custom Url cannot be used as it is already being used",
                        HttpStatus.CONFLICT);
            }
            aliasUrl = custom;
        } else {
            aliasUrl = shorteningUtil.generateRandomShortUrl();
        }
        try {
            UrlDetails savedUrlDetails = urlDetailsRepository.save(new UrlDetails(aliasUrl, inputUrl, redirect));
            if (savedUrlDetails != null) {
                return new ResponseEntity<>("Custom url assigned - " + APP_URL + aliasUrl + " for provided url " + inputUrl, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            log.error("Some error Occured. Please Try Again.", e);
        }
        return new ResponseEntity<>("Some error Occured. Please Try Again.", HttpStatus.NOT_ACCEPTABLE);
    }
}