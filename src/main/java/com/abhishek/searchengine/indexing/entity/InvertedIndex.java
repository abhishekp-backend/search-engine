package com.abhishek.searchengine.indexing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "inverted_index",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"term", "documentId"}
        ))
@Getter
@Setter
public class InvertedIndex {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String term;

    @Column(nullable = false)
    private UUID documentId;

    @Column(nullable = false)
    private Integer termFrequency;
}
