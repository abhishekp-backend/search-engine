package com.abhishek.searchengine.indexing.semantic.dto;

public record OllamaEmbeddingRequest(
        String model,
        String input
) {}