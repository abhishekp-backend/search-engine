package com.abhishek.searchengine.discovery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "discovered_urls")
@Getter
@Setter
public class DiscoveredUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_page_id", nullable = false)
    private Page sourcePage;

    @Column(name = "discovered_at", nullable = false)
    private LocalDateTime discoveredAt;
}