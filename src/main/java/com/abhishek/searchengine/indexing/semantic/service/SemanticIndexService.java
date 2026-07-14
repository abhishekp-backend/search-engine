package com.abhishek.searchengine.indexing.semantic.service;

import com.abhishek.searchengine.crawler.entity.CrawledPage;
import com.abhishek.searchengine.indexing.semantic.chunk.TextChunker;
import com.abhishek.searchengine.indexing.semantic.dto.Chunk;
import com.abhishek.searchengine.indexing.semantic.entity.DocumentChunk;
import com.abhishek.searchengine.indexing.semantic.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SemanticIndexService {

    private static final Logger log = LoggerFactory.getLogger(SemanticIndexService.class);
    private final TextChunker textChunker;
    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository documentChunkRepository;

    public void index(CrawledPage page) {

        List<Chunk> chunks = textChunker.chunk(page);

        for (Chunk chunk : chunks) {

            List<Float> vector =
                    embeddingService.embed(chunk.text());

            DocumentChunk documentChunk = new DocumentChunk();

            documentChunk.setPageId(page.getId());
            documentChunk.setChunkIndex(chunk.chunkIndex());
            documentChunk.setText(chunk.text());

            float[] embeddingArray = new float[vector.size()];

            for (int i = 0; i < vector.size(); i++) {
                embeddingArray[i] = vector.get(i);
            }

            documentChunk.setEmbedding(embeddingArray);

            System.out.println("------------------------------------");
            System.out.println(documentChunk.getEmbedding().getClass());
            System.out.println("------------------------------------");

            SemanticIndexService.log.info("-----------------Embeddings length: {}", embeddingArray.length);

            documentChunkRepository.save(documentChunk);
        }
    }
}