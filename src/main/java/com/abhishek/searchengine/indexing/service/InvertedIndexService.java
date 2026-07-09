package com.abhishek.searchengine.indexing.service;

import com.abhishek.searchengine.indexing.entity.InvertedIndex;
import com.abhishek.searchengine.indexing.repository.InvertedIndexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InvertedIndexService {

    private final InvertedIndexRepository invertedIndexRepository;

    public void index(UUID documentId, List<String> tokens) {

        Map<String, Integer> frequencyMap = new HashMap<>();

        for (String token : tokens) {
            frequencyMap.put(token,
                    frequencyMap.getOrDefault(token, 0) + 1);
        }

        List<InvertedIndex> indexes = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {

            InvertedIndex invertedIndex = new InvertedIndex();

            invertedIndex.setTerm(entry.getKey());
            invertedIndex.setDocumentId(documentId);
            invertedIndex.setTermFrequency(entry.getValue());

            indexes.add(invertedIndex);
        }

        invertedIndexRepository.saveAll(indexes);
    }
}