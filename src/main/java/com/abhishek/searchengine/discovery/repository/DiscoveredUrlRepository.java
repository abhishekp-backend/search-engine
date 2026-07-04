package com.abhishek.searchengine.discovery.repository;

import com.abhishek.searchengine.discovery.entity.DiscoveredUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscoveredUrlRepository extends JpaRepository<DiscoveredUrl, Long> {
    boolean existsByUrl(String url);

    Optional<DiscoveredUrl> findByUrl(String url);
}
