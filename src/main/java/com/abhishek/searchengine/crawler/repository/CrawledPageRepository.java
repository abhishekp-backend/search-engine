package com.abhishek.searchengine.crawler.repository;

import com.abhishek.searchengine.crawler.entity.CrawledPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CrawledPageRepository extends JpaRepository<CrawledPage, UUID> {
    boolean existsByUrl(String url);
}
