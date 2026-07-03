package com.abhishek.searchengine.crawler.controller;

import com.abhishek.searchengine.crawler.dto.CrawlRequest;
import com.abhishek.searchengine.crawler.dto.CrawlResponse;
import com.abhishek.searchengine.crawler.service.CrawlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CrawlController {

    private final CrawlService crawlService;

    @PostMapping("/crawl")
    public ResponseEntity<CrawlResponse> crawl(@RequestBody @Valid CrawlRequest request) throws IOException {
        return ResponseEntity.ok(crawlService.crawl(request.url()));
    }

}
