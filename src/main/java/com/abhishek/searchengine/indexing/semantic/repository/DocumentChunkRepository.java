package com.abhishek.searchengine.indexing.semantic.repository;

import com.abhishek.searchengine.indexing.semantic.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, UUID> {

    @Query(value = """
            SELECT *
            FROM document_chunk
            ORDER BY embedding <=> CAST(:vector AS vector)
            LIMIT :limit
            """, nativeQuery = true)
    List<DocumentChunk> findSimilar(
            @Param("vector") float[] vector,
            @Param("limit") int limit
    );
}