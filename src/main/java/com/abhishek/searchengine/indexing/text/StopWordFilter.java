package com.abhishek.searchengine.indexing.text;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class StopWordFilter {

    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the",
            "is", "are", "was", "were", "be", "been", "being",
            "am",
            "of", "to", "for", "from",
            "and", "or", "but",
            "in", "on", "at", "by", "with", "into", "over",
            "as", "if", "than", "then",
            "this", "that", "these", "those",
            "it", "its",
            "he", "she", "they", "them", "their",
            "you", "your",
            "we", "our",
            "i", "me", "my",
            "have", "has", "had",
            "do", "does", "did",
            "can", "could", "will", "would",
            "shall", "should",
            "may", "might",
            "must",
            "not"
    );

    public List<String> filter(List<String> tokens) {
        return tokens.stream()
                .filter(token -> !STOP_WORDS.contains(token))
                .toList();
    }
}
