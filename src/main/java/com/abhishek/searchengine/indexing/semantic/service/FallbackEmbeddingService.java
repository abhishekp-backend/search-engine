package com.abhishek.searchengine.indexing.semantic.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@Primary
public class FallbackEmbeddingService implements EmbeddingService {

//    private final GeminiEmbeddingService geminiEmbeddingService;
    private final OllamaEmbeddingService ollamaEmbeddingService;

    @Override
    public List<Float> embed (String text) {
        try {
            throw new Exception();
//            return geminiEmbeddingService.embed(text);
        }

        catch (Exception e) {
            log.warn("Gemini unavailable, falling back to Ollama", e);
            return ollamaEmbeddingService.embed(text);
        }
    }
}