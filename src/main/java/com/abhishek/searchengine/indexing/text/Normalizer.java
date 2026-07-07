package com.abhishek.searchengine.indexing.text;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Normalizer {
    public List<String> normalize(List<String> tokens) {
        return tokens.stream()
                .map(String::toLowerCase)
                .map(String::trim)
                .filter(token -> !token.isBlank())
                .toList();
    }
}
