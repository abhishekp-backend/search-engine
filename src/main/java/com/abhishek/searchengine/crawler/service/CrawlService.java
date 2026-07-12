package com.abhishek.searchengine.crawler.service;

import com.abhishek.searchengine.crawler.config.CrawlerProperties;
import com.abhishek.searchengine.crawler.dto.CrawlTask;
import com.abhishek.searchengine.crawler.entity.CrawledPage;
import com.abhishek.searchengine.crawler.messaging.CrawlProducer;
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
    private final CrawlProducer crawlProducer;

    public void crawl(CrawlTask task) {

        if (task.depth() > crawlerProperties.maxDepth()) {
            return;
        }

        try {

            URI taskUri = URI.create(task.url());

            if (!task.allowedHost().equalsIgnoreCase(taskUri.getHost())) {
                return;
            }

            // Skip if already crawled
            if (crawledPageRepository.existsByUrl(task.url())) {
                return;
            }

            Connection.Response response = Jsoup.connect(task.url())
                    .userAgent(crawlerProperties.userAgent())
                    .timeout(crawlerProperties.timeout())
                    .execute();

            if (response.statusCode() != 200) {
                throw new IOException(
                        "Unexpected HTTP status "
                                + response.statusCode()
                                + " while crawling "
                                + task.url()
                );
            }

            Document document = response.parse();

            CrawledPage crawledPage = new CrawledPage();
            crawledPage.setUrl(task.url());
            crawledPage.setTitle(document.title());
            crawledPage.setHtml(document.html());
            crawledPage.setStatusCode(response.statusCode());

            crawledPageRepository.save(crawledPage);

            int totalTerms = indexService.indexDocument(crawledPage);

            crawledPage.setTotalTerms(totalTerms);
            crawledPageRepository.save(crawledPage);

            Set<String> links = discoveryService.extractLinks(document);

            for (String link : links) {

                try {

                    URI linkUri = URI.create(link);

                    if (!task.allowedHost().equalsIgnoreCase(linkUri.getHost())) {
                        continue;
                    }

                    if (discoveredUrlRepository.existsByUrl(link)) {
                        continue;
                    }

                    DiscoveredUrl discoveredUrl = new DiscoveredUrl();
                    discoveredUrl.setUrl(link);
                    discoveredUrl.setSourcePage(crawledPage);

                    discoveredUrlRepository.save(discoveredUrl);

                    crawlProducer.publish(
                            new CrawlTask(
                                    link,
                                    task.depth() + 1,
                                    task.allowedHost()
                            )
                    );

                } catch (Exception ignored) {
                    log.warn("Skipping malformed URL: {}", link);
                }
            }

        } catch (Exception e) {
            log.error("Failed to crawl {}", task.url(), e);
        }
    }
}