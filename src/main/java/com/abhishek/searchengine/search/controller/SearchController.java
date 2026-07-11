package com.abhishek.searchengine.search.controller;

import com.abhishek.searchengine.search.dto.SearchRequest;
import com.abhishek.searchengine.search.dto.SearchResponse;
import com.abhishek.searchengine.search.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/health")
    public String health() {
        return "Search Engine is working!";
    }

    @PostMapping("/search")
    public ResponseEntity<List<SearchResponse>> search(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(searchService.search(request.query()));
    }

    @GetMapping("/redis")
    public String testRedis() {
        redisTemplate.opsForValue().set("test", "Hello Redis");
        return redisTemplate.opsForValue().get("test");
    }
}
