package com.abhishek.searchengine.indexing.controller;

import com.abhishek.searchengine.indexing.semantic.service.FallbackEmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testLLM")
@RequiredArgsConstructor
public class TestingEmbedding {

    private final FallbackEmbeddingService fallbackEmbeddingService;

    @GetMapping("/")
    public List<Float> testLLM() {
        return fallbackEmbeddingService.embed("Google Search");
    }
}
