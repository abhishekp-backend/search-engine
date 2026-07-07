package com.abhishek.searchengine.indexing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inverted_index",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"term", "documentId"}
        ))
@Getter
@Setter
public class InvertedIndex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String term;

    @Column(nullable = false)
    private Long documentId;

    @Column(nullable = false)
    private Integer termFrequency;
}
