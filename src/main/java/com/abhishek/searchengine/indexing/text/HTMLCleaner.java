package com.abhishek.searchengine.indexing.text;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class HTMLCleaner {
    public String clean(String html) {
        return Jsoup.parse(html).text();
    }
}
