package com.abhishek.searchengine.search.service;

import com.abhishek.searchengine.crawler.entity.CrawledPage;
import com.abhishek.searchengine.crawler.repository.CrawledPageRepository;
import com.abhishek.searchengine.indexing.entity.InvertedIndex;
import com.abhishek.searchengine.indexing.repository.InvertedIndexRepository;
import com.abhishek.searchengine.search.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final InvertedIndexRepository invertedIndexRepository;
    private final CrawledPageRepository crawledPageRepository;

    public List<SearchResponse> search(String query) {

        int totalDocuments = Math.toIntExact(crawledPageRepository.count());
        query = query.trim().toLowerCase(Locale.ROOT);
        List<InvertedIndex> postings = invertedIndexRepository.findByTerm(query);
        int df = postings.size();
        double idf = Math.log((double) totalDocuments / df);

        Map<UUID, Double> scores = new HashMap<>();

        for (InvertedIndex posting: postings) {
            Optional<CrawledPage> page = crawledPageRepository.findById(posting.getDocumentId());

            double tf = (double) posting.getTermFrequency() / page.get().getTotalTerms();

            double score = tf * idf;

            scores.put(posting.getDocumentId(), score);
        }

        List<Map.Entry<UUID, Double>> rankedDocuments = scores.entrySet()
                .stream()
                .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
                .toList();

        List<CrawledPage> pages =
                crawledPageRepository.findAllById(scores.keySet());

        Map<UUID, CrawledPage> pageMap = pages.stream()
                .collect(Collectors.toMap(
                        CrawledPage::getId,
                        page -> page
                ));

        return rankedDocuments.stream()
                .map(entry -> {

                    CrawledPage page = pageMap.get(entry.getKey());

                    return new SearchResponse(
                            page.getId(),
                            page.getTitle(),
                            page.getUrl(),
                            createSnippet(page.getHtml())
                    );
                })
                .toList();
    }

    private String createSnippet(String content) {
        if (content == null) {
            return "";
        }

        return content.length() <= 200
                ? content
                : content.substring(0, 200) + "...";
    }
}