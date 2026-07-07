package com.abhishek.searchengine.indexing.text;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class StopWordFilter {

    private static final Set<String> STOP_WORDS = Set.of(
            "a",
            "an",
            "the",
            "is",
            "are",
            "of",
            "to",
            "for",
            "and",
            "in",
            "on"
    );

    public List<String> filter(List<String> tokens) {
        return tokens.stream()
                .filter(token -> !STOP_WORDS.contains(token))
                .toList();
    }
}
