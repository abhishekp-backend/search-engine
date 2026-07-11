package com.abhishek.searchengine.search.service;

import com.abhishek.searchengine.crawler.entity.CrawledPage;
import com.abhishek.searchengine.crawler.repository.CrawledPageRepository;
import com.abhishek.searchengine.indexing.entity.InvertedIndex;
import com.abhishek.searchengine.indexing.repository.InvertedIndexRepository;
import com.abhishek.searchengine.search.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final InvertedIndexRepository invertedIndexRepository;
    private final CrawledPageRepository crawledPageRepository;
    private final RedisTemplate<String, List<SearchResponse>> redisTemplate;

    public List<SearchResponse> search(String query) {
        query = query.trim().toLowerCase(Locale.ROOT);
        String key = "search:" + query.toLowerCase();

        try {
            List<SearchResponse> cached =
                    redisTemplate.opsForValue().get(key);

            if (cached != null) {
                return cached;
            }
        } catch (Exception e) {
            log.error(e.toString());
        }

        int totalDocuments = Math.toIntExact(crawledPageRepository.count());
        List<InvertedIndex> postings = invertedIndexRepository.findByTerm(query);

        if (postings.isEmpty()) {
            return Collections.emptyList();
        }

        int df = postings.size();
        double idf = Math.log((double) totalDocuments / df);

        List<UUID> documentIds = postings.stream()
                .map(InvertedIndex::getDocumentId)
                .toList();

        List<CrawledPage> pages = crawledPageRepository.findAllById(documentIds);

        Map<UUID, CrawledPage> pageMap = pages.stream()
                .collect(Collectors.toMap(
                        CrawledPage::getId,
                        page -> page
                ));

        Map<UUID, Double> scores = new HashMap<>();

        for (InvertedIndex posting: postings) {
            CrawledPage page = pageMap.get(posting.getDocumentId());

            if (page == null) {
                continue;
            }

            double tf = (double) posting.getTermFrequency() / page.getTotalTerms();
            scores.put(posting.getDocumentId(), tf * idf);
        }

        String finalQuery = query;

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
                            createSnippet(page.getHtml(), finalQuery)
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
            return content.substring(0, 200);
        }

        int start = Math.max(0, index - 80);
        int end = Math.min(content.length(), index + query.length() + 80);

        return content.length() <= end
                ? content
                : content.substring(start, end) + "...";
    }
}