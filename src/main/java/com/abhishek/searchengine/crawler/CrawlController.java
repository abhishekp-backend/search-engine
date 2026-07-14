package com.abhishek.searchengine.crawler;

import com.abhishek.searchengine.crawler.dto.CrawlRequest;
import com.abhishek.searchengine.crawler.dto.CrawlTask;
import com.abhishek.searchengine.crawler.messaging.CrawlProducer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CrawlController {

    private final CrawlProducer crawlProducer;

    @PostMapping("/crawl")
    public ResponseEntity<String> crawl(@RequestBody @Valid CrawlRequest request) {

        URI taskUri = URI.create(request.url());
        crawlProducer.publish(new CrawlTask(request.url(), 0, taskUri.getHost()));

        return ResponseEntity.accepted()
                .body("Crawl request submitted successfully.");
    }

}
