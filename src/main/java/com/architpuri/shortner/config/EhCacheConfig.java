package com.architpuri.shortner.config;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.internal.statistics.DefaultStatisticsService;
import org.ehcache.core.spi.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class EhCacheConfig {

    @Bean
    public StatisticsService getStatisticsService() {
        return new DefaultStatisticsService();
    }

    @Bean
    public CacheManager getCacheManager(@Autowired final StatisticsService statisticsService) {
        final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().using(statisticsService).build();
        cacheManager.init();
        return cacheManager;
    }

    @Bean(name = "urlDetailCache")
    public Cache<String, String> urlDetail(@Autowired final CacheManager cacheManager) {
        final CacheConfiguration<String, String> configuration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(1000))
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(300))).build();
        return cacheManager.createCache("urlDetailCache", configuration);
    }

}
