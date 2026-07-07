package com.abhishek.searchengine.crawler.repository;

import com.abhishek.searchengine.crawler.entity.CrawledPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawledPageRepository extends JpaRepository<CrawledPage, Long> {
}
