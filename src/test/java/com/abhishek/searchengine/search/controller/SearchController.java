package com.abhishek.searchengine.search.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SearchController {
    @GetMapping("/health")
    public String health() {
        return "Search Engine is working!";
    }
}
