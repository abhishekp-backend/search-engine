package com.abhishek.searchengine.indexing.semantic.chunk;

import com.abhishek.searchengine.crawler.entity.CrawledPage;
import com.abhishek.searchengine.indexing.semantic.dto.Chunk;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FixedSizeChunker implements TextChunker {

    private static final int CHUNK_SIZE = 300;

    @Override
    public List<Chunk> chunk(CrawledPage page) {

        String text = Jsoup.parse(page.getHtml()).text();

        String[] words = text.split("\\s+");

        List<Chunk> chunks = new ArrayList<>();

        int chunkIndex = 0;

        for (int start = 0; start < words.length; start += CHUNK_SIZE) {

            int end = Math.min(start + CHUNK_SIZE, words.length);

            String chunkText = String.join(
                    " ",
                    java.util.Arrays.copyOfRange(words, start, end)
            );

            chunks.add(
                    new Chunk(
                            page.getId(),
                            chunkIndex++,
                            chunkText
                    )
            );
        }

        return chunks;
    }
}