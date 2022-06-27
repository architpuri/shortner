package com.architpuri.shortner.repo;

import com.architpuri.shortner.model.UrlDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface UrlDetailsRepository extends JpaRepository<UrlDetails, String> {

    UrlDetails findByAliasUrl(String aliasUrl);

    UrlDetails findByOriginalUrl(String originalUrl);

    List<UrlDetails> findByExpiryBefore(Long currentTime);

    void deleteAllByIdIn(Set<Long> expiredRecordsSet);
}
