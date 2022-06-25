package com.architpuri.shortner.repo;

import com.architpuri.shortner.model.UrlDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlDetailsRepository extends JpaRepository<UrlDetails, String> {

    UrlDetails findByAliasUrl(String aliasUrl);

    UrlDetails findByOriginalUrl(String originalUrl);
}
