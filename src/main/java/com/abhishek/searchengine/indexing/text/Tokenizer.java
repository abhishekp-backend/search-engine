package com.abhishek.searchengine.indexing.text;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Tokenizer {
    public List<String> clean(String text) {
        return Arrays.stream(text.split("[^A-Za-z0-9]+"))
                .filter(token -> !token.isBlank())
                .toList();
    }
}
