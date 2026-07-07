package com.abhishek.searchengine.discovery.entity;

import com.abhishek.searchengine.crawler.entity.CrawledPage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "discovered_urls")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class DiscoveredUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_page_id", nullable = false)
    private CrawledPage sourcePage;

    @Column(name = "discovered_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime discoveredAt;
}