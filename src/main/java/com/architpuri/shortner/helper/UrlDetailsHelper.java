package com.architpuri.shortner.helper;

import com.architpuri.shortner.model.UrlDetails;
import com.architpuri.shortner.repo.UrlDetailsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UrlDetailsHelper {

    @Autowired
    private UrlDetailsRepository urlDetailsRepository;

    @Autowired
    private CacheHelper cacheHelper;


    public UrlDetails getUrlDetailsByAlias(final String aliasUrl) {
        UrlDetails urlDetailsCached = cacheHelper.getFromCache(aliasUrl);
        if (urlDetailsCached != null) {
            return urlDetailsCached;
        }
        UrlDetails urlDetails = urlDetailsRepository.findByAliasUrl(aliasUrl);
        if (urlDetails != null) {
            cacheHelper.addToCache(urlDetails);
        }
        return urlDetails;
    }

    public UrlDetails getUrlDetailsByOriginal(final String originalUrl) {
        return urlDetailsRepository.findByOriginalUrl(originalUrl);
    }

    public UrlDetails saveDetails(final UrlDetails urlDetails) {
        try {
            UrlDetails savedUrlDetails = urlDetailsRepository.save(urlDetails);
            return savedUrlDetails;
        } catch (Exception e) {
            log.error("Exception caused while saving details", e);
        }
        return null;
    }

    public void deleteDetails(final UrlDetails urlDetails) {
        cacheHelper.removeFromCache(urlDetails);
        urlDetailsRepository.delete(urlDetails);
    }
}
