package com.abhishek.searchengine.indexing.repository;

import com.abhishek.searchengine.indexing.entity.InvertedIndex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvertedIndexRepository extends JpaRepository<InvertedIndex, Long> {
}
