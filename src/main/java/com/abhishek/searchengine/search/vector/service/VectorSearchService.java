package com.abhishek.searchengine.search.vector.service;

import com.abhishek.searchengine.crawler.entity.CrawledPage;
import com.abhishek.searchengine.crawler.repository.CrawledPageRepository;
import com.abhishek.searchengine.indexing.semantic.entity.DocumentChunk;
import com.abhishek.searchengine.indexing.semantic.repository.DocumentChunkRepository;
import com.abhishek.searchengine.indexing.semantic.service.EmbeddingService;
import com.abhishek.searchengine.search.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VectorSearchService {

    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository documentChunkRepository;
    private final CrawledPageRepository crawledPageRepository;

    public List<SearchResponse> search(String query) {

        query = query.toLowerCase(Locale.ROOT);

        List<Float> queryEmbedding =
                embeddingService.embed(query);

        float[] vector = new float[queryEmbedding.size()];

        for(int i = 0; i < queryEmbedding.size(); i++) {
            vector[i] = queryEmbedding.get(i);
        }


        List<UUID> similarSearches = documentChunkRepository.findSimilar(vector, 5).
                stream()
                .map(DocumentChunk::getPageId)
                .toList();

        List<CrawledPage> similarPages = crawledPageRepository.findAllById(similarSearches);
        String finalQuery = query;

        return similarPages.
                stream()
                .map(entry -> new SearchResponse(
                        entry.getId(),
                        entry.getTitle(),
                        entry.getUrl(),
                        createSnippet(entry.getHtml(), finalQuery)
                ))
                .toList();
    }

    private String createSnippet(String content, String query) {
        if (content == null) {
            return "";
        }

        content = Jsoup.parse(content).text();

        int index = content.toLowerCase().indexOf(query.toLowerCase());

        if (index == -1) {
            if (content.length() > 200) return content.substring(0, 200);
            else return content;
        }

        int start = Math.max(0, index - 80);
        int end = Math.min(content.length(), index + query.length() + 80);

        return content.length() <= end
                ? content
                : content.substring(start, end) + "...";
    }
}