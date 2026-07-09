package com.abhishek.searchengine.search.dto;

import java.util.UUID;

public record SearchResponse(
        UUID id,
        String title,
        String url,
        String snippet
) {
}