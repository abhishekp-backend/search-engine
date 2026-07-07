package com.abhishek.searchengine.crawler.service;

import com.abhishek.searchengine.crawler.config.CrawlerProperties;
import com.abhishek.searchengine.crawler.dto.CrawlRequest;
import com.abhishek.searchengine.crawler.dto.CrawlResponse;
import com.abhishek.searchengine.crawler.dto.CrawlTask;
import com.abhishek.searchengine.crawler.entity.CrawledPage;
import com.abhishek.searchengine.crawler.repository.CrawledPageRepository;
import com.abhishek.searchengine.discovery.entity.DiscoveredUrl;
import com.abhishek.searchengine.discovery.repository.DiscoveredUrlRepository;
import com.abhishek.searchengine.discovery.service.DiscoveryService;
import com.abhishek.searchengine.indexing.service.IndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlService {

    private final DiscoveryService discoveryService;
    private final CrawlerProperties crawlerProperties;
    private final CrawledPageRepository crawledPageRepository;
    private final DiscoveredUrlRepository discoveredUrlRepository;
    private final IndexService indexService;

    public CrawlResponse crawl(CrawlRequest request) {

        Queue<CrawlTask> frontier = new ArrayDeque<>();

        // URLs that have already been queued
        Set<String> discovered = new HashSet<>();

        // URLs that have actually been crawled
        Set<String> visited = new HashSet<>();

        frontier.offer(new CrawlTask(request.url(), 0));
        discovered.add(request.url());

        URI seedUri = URI.create(request.url());
        String allowedHost = seedUri.getHost();

        while (!frontier.isEmpty()) {

            CrawlTask task = frontier.poll();

            URI taskUri = URI.create(task.url());

            if (!allowedHost.equalsIgnoreCase(taskUri.getHost())) {
                continue;
            }

            if (task.depth() > crawlerProperties.maxDepth()) {
                continue;
            }

            if (!visited.add(task.url())) {
                continue;
            }

            try {

                Connection.Response response = Jsoup.connect(task.url())
                        .userAgent(crawlerProperties.userAgent())
                        .timeout(crawlerProperties.timeout())
                        .execute();

                if (response.statusCode() != 200) {
                    throw new IOException(
                            "Unexpected HTTP status " +
                                    response.statusCode() +
                                    " while crawling " +
                                    task.url()
                    );
                }

                Document document = response.parse();

                CrawledPage crawledPage = new CrawledPage();
                crawledPage.setUrl(task.url());
                crawledPage.setTitle(document.title());
                crawledPage.setHtml(document.html());
                crawledPage.setStatusCode(response.statusCode());

                crawledPageRepository.save(crawledPage);

                indexService.indexDocument(crawledPage);

                Set<String> links = discoveryService.extractLinks(document);

                for (String link : links) {

                    try {

                        URI linkUri = URI.create(link);

                        if (!allowedHost.equalsIgnoreCase(linkUri.getHost())) {
                            continue;
                        }

                        if (discovered.add(link)) {

                            DiscoveredUrl discoveredUrl = new DiscoveredUrl();
                            discoveredUrl.setUrl(link);
                            discoveredUrl.setSourcePage(crawledPage);

                            discoveredUrlRepository.save(discoveredUrl);

                            frontier.offer(
                                    new CrawlTask(
                                            link,
                                            task.depth() + 1
                                    )
                            );
                        }

                    } catch (Exception ignored) {
                        log.warn("Skipping malformed URL: {}", link);
                    }
                }

            } catch (Exception e) {
                log.error("Failed to crawl {}", task.url(), e);
            }
        }

        return new CrawlResponse(
                visited.size(),
                "Crawl Completed"
        );
    }
}