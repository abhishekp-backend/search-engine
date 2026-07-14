package com.abhishek.searchengine.indexing.semantic.service;

import com.abhishek.searchengine.indexing.semantic.dto.OllamaEmbeddingRequest;
import com.abhishek.searchengine.indexing.semantic.dto.OllamaEmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OllamaEmbeddingService implements EmbeddingService {

    private final RestClient restClient;

    @Override
    public List<Float> embed(String text) {
        OllamaEmbeddingRequest request = new OllamaEmbeddingRequest(
                "nomic-embed-text",
                text
        );

        OllamaEmbeddingResponse response = restClient.post()
                .uri("http://localhost:11434/api/embed")
                .body(request)
                .retrieve()
                .body(OllamaEmbeddingResponse.class);

        assert response != null;
        return response.embeddings().getFirst();
    }
}