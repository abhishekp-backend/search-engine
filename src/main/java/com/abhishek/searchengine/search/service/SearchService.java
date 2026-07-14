package com.abhishek.searchengine.search.service;

import com.abhishek.searchengine.crawler.entity.CrawledPage;
import com.abhishek.searchengine.crawler.repository.CrawledPageRepository;
import com.abhishek.searchengine.indexing.entity.InvertedIndex;
import com.abhishek.searchengine.indexing.repository.InvertedIndexRepository;
import com.abhishek.searchengine.indexing.service.TextProcessingPipeline;
import com.abhishek.searchengine.search.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final InvertedIndexRepository invertedIndexRepository;
    private final CrawledPageRepository crawledPageRepository;
    private final RedisTemplate<String, List<SearchResponse>> redisTemplate;
    private final TextProcessingPipeline textProcessingPipeline;

    public List<SearchResponse> search(String query) {

        String originalQuery = query.trim();
        String key = "search:" + originalQuery.toLowerCase(Locale.ROOT);

        try {
            List<SearchResponse> cached = redisTemplate.opsForValue().get(key);

            if (cached != null) {
                return cached;
            }
        } catch (Exception e) {
            log.error(e.toString());
        }

        List<String> terms = textProcessingPipeline.process(originalQuery);

        if (terms.isEmpty()) {
            return Collections.emptyList();
        }

        int totalDocuments = Math.toIntExact(crawledPageRepository.count());

        Map<UUID, Double> scores = new HashMap<>();
        Map<UUID, CrawledPage> pageMap = new HashMap<>();

        for (String term : terms) {

            List<InvertedIndex> postings = invertedIndexRepository.findByTerm(term);

            if (postings.isEmpty()) {
                continue;
            }

            int df = postings.size();
            double idf = Math.log((double) totalDocuments / df);

            List<UUID> ids = postings.stream()
                    .map(InvertedIndex::getDocumentId)
                    .toList();

            List<CrawledPage> pages = crawledPageRepository.findAllById(ids);

            for (CrawledPage page : pages) {
                pageMap.putIfAbsent(page.getId(), page);
            }

            for (InvertedIndex posting : postings) {

                CrawledPage page = pageMap.get(posting.getDocumentId());

                if (page == null) {
                    continue;
                }

                double tf = (double) posting.getTermFrequency() / page.getTotalTerms();

                scores.merge(
                        posting.getDocumentId(),
                        tf * idf,
                        Double::sum
                );
            }
        }

        if (scores.isEmpty()) {
            return Collections.emptyList();
        }

        List<SearchResponse> results = scores.entrySet()
                .stream()
                .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
                .limit(20)
                .map(entry -> {

                    CrawledPage page = pageMap.get(entry.getKey());

                    return new SearchResponse(
                            page.getId(),
                            page.getTitle(),
                            page.getUrl(),
                            createSnippet(page.getHtml(), originalQuery)
                    );
                })
                .toList();

        redisTemplate.opsForValue().set(
                key,
                results,
                Duration.ofMinutes(10)
        );

        return results;
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