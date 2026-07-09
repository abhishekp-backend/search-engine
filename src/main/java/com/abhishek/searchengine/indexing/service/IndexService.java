package com.abhishek.searchengine.indexing.service;

import com.abhishek.searchengine.crawler.entity.CrawledPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexService {

    private final TextProcessingPipeline textProcessingPipeline;
    private final InvertedIndexService invertedIndexService;

    public int indexDocument(CrawledPage page) {

        List<String> tokens = textProcessingPipeline.process(page.getHtml());

        invertedIndexService.index(page.getId(), tokens);

        return tokens.size();
    }
}