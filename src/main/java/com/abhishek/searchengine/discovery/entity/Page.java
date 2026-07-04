package com.abhishek.searchengine.discovery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pages")
@Getter
@Setter
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String url;

    private String title;

    @Lob
    private String html;

    private Integer statusCode;

    @Column(name = "crawled_at")
    private LocalDateTime crawledAt;

}
