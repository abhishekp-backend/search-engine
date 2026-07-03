package com.abhishek.searchengine.crawler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "crawled_pages")
public class CrawledPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String html;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private LocalDateTime crawledAt;

    @PrePersist
    public void onCreate() {
        this.crawledAt = LocalDateTime.now();
    }
}
