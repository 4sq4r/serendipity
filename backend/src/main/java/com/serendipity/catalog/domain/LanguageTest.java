package com.serendipity.catalog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "language_tests")
public class LanguageTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    protected LanguageTest() {
    }

    public LanguageTest(Language language, String name, String description) {
        this.language = language;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Language getLanguage() {
        return language;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
