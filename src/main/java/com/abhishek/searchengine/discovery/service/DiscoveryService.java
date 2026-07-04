package com.abhishek.searchengine.discovery.service;

import com.abhishek.searchengine.discovery.entity.Page;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Service
@NoArgsConstructor
@RequestMapping("/")
public class DiscoveryService {
    public Set<String> extractLinks(Document document) {

        Set<String> links = new HashSet<>();

        Elements anchorTags = document.select("a[href]");

        for (Element anchor : anchorTags) {

            String link = anchor.absUrl("href");

            if (link.isEmpty()) {
                continue;
            }

            if (link.startsWith("javascript:") ||
                    link.startsWith("mailto:") ||
                    link.startsWith("#")) {
                continue;
            }

            links.add(link);
        }

        return links;
    }
}
