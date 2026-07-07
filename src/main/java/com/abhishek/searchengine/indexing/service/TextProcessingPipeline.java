package com.abhishek.searchengine.indexing.service;

import com.abhishek.searchengine.indexing.text.HTMLCleaner;
import com.abhishek.searchengine.indexing.text.Normalizer;
import com.abhishek.searchengine.indexing.text.StopWordFilter;
import com.abhishek.searchengine.indexing.text.Tokenizer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TextProcessingPipeline {

    private final HTMLCleaner htmlCleaner;
    private final Tokenizer tokenizer;
    private final Normalizer normalizer;
    private final StopWordFilter stopWordFilter;

    public List<String> process(String html) {

        String cleanedText = htmlCleaner.clean(html);

        List<String> tokens = tokenizer.clean(cleanedText);

        List<String> normalized = normalizer.normalize(tokens);

        return stopWordFilter.filter(normalized);
    }
}
