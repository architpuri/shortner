package com.architpuri.shortner.helper;

import com.architpuri.shortner.model.UrlDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheHelper {

    @Autowired
    private Cache<String, String> urlDetailCache;

    private ObjectMapper objectMapper = new ObjectMapper();

    public UrlDetails getFromCache(final String aliasUrl) {
        if (urlDetailCache.containsKey(aliasUrl)) {
            String value = urlDetailCache.get(aliasUrl);
            try {
                UrlDetails details = objectMapper.readValue(value, UrlDetails.class);
                return details;
            } catch (JsonProcessingException e) {
                log.error("Json Processing Exception while getting data from urlDetailsCache ", e);
            }
        }
        return null;
    }

    @Async
    public void addToCache(final UrlDetails urlDetails) {
        try {
            urlDetailCache.put(urlDetails.getAliasUrl(), objectMapper.writeValueAsString(urlDetails));
        } catch (JsonProcessingException e) {
            log.error("Json Processing Exception while putting data into urlDetailsCache ", e);
        }
    }

    public void removeFromCache(final UrlDetails urlDetails) {
        if (urlDetailCache.containsKey(urlDetails.getAliasUrl())) {
            urlDetailCache.remove(urlDetails.getAliasUrl());
        }
    }
}
