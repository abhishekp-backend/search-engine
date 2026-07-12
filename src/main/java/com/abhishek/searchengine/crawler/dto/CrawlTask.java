package com.abhishek.searchengine.crawler.dto;

public record CrawlTask (String url, int depth, String allowedHost) {
}
