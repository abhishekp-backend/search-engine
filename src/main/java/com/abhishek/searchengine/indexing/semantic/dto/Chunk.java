package com.abhishek.searchengine.indexing.semantic.dto;

import java.util.UUID;

public record Chunk(
        UUID pageId,
        int chunkIndex,
        String text
) {
}
