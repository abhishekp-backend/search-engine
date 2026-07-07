package com.abhishek.searchengine.crawler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crawler")
public record CrawlerProperties (
        int maxDepth,
        String userAgent,
        int timeout
) {}
