package com.abhishek.searchengine.crawler.service;

import com.abhishek.searchengine.crawler.dto.CrawlResponse;
import lombok.NoArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@NoArgsConstructor
@Service
public class CrawlService {
    public CrawlResponse crawl(String url) throws IOException {
        Connection.Response res = Jsoup.connect(url)
                .userAgent("SearchEngineBot/1.0")
                .timeout(1000)
                .execute();

        Document doc = res.parse();

        return new CrawlResponse(
                doc.title(),
                res.statusCode()
        );
    }
}
