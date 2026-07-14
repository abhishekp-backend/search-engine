package com.abhishek.searchengine.indexing.semantic.dto;

import java.util.List;

public record OllamaEmbeddingResponse(
        String model,
        List<List<Float>> embeddings
) {}