package com.architpuri.shortner.helper;

import com.architpuri.shortner.model.UrlDetails;
import com.architpuri.shortner.repo.UrlDetailsRepository;
import com.architpuri.shortner.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public void cleanExpiredRecords() {
        log.info("Cleaning Records");
        List<UrlDetails> urlDetailsList = urlDetailsRepository.findByExpiryBefore(CommonUtils.getCurrentEpochTime());
        Set<Long> expiredRecordsSet = urlDetailsList.stream().filter(detail -> (cacheHelper.getFromCache(detail.getAliasUrl()) == null))
                .map(detail -> detail.getId()).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(expiredRecordsSet)) {
            log.info("{} expired records found cleaning.", expiredRecordsSet.size());
            urlDetailsRepository.deleteAllByIdIn(expiredRecordsSet);
        }
    }
}
