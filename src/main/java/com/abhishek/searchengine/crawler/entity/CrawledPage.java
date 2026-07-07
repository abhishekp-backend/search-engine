package com.abhishek.searchengine.crawler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "crawled_pages")
@EntityListeners(AuditingEntityListener.class)
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
    private Integer statusCode;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime crawledAt;
}
