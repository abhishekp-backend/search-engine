package com.abhishek.searchengine.indexing.semantic.chunk;


import com.abhishek.searchengine.crawler.entity.CrawledPage;
import com.abhishek.searchengine.indexing.semantic.dto.Chunk;

import java.util.List;

public interface TextChunker {
    List<Chunk> chunk(CrawledPage page);
}