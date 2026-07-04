package com.abhishek.searchengine.crawler.service;

import com.abhishek.searchengine.crawler.dto.CrawlResponse;
import com.abhishek.searchengine.discovery.service.DiscoveryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CrawlService {

    private final DiscoveryService discoveryService;

    public CrawlResponse crawl(String url) throws IOException {
        Connection.Response res = Jsoup.connect(url)
                .userAgent("SearchEngineBot/1.0")
                .timeout(5000)
                .execute();

        Document doc = res.parse();

        Set<String> pages = discoveryService.extractLinks(doc);

        return new CrawlResponse(
                doc.title(),
                res.statusCode()
        );
    }
}
