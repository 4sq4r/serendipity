package com.serendipity.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.serendipity.RepositoryTest;
import com.serendipity.catalog.domain.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

@RepositoryTest
class LanguageRepositoryTest {

    private static final String GERMAN_CODE = "de";
    private static final String GERMAN_NAME = "German";
    private static final String OTHER_NAME = "Deutsch";

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void savesAndLoadsLanguage() {
        Long id = languageRepository.saveAndFlush(new Language(GERMAN_CODE, GERMAN_NAME)).getId();
        entityManager.clear();

        Language loaded = languageRepository.findById(id).orElseThrow();

        assertThat(loaded.getCode()).isEqualTo(GERMAN_CODE);
        assertThat(loaded.getName()).isEqualTo(GERMAN_NAME);
    }

    @Test
    void rejectsDuplicateLanguageCode() {
        languageRepository.saveAndFlush(new Language(GERMAN_CODE, GERMAN_NAME));

        assertThatThrownBy(() -> languageRepository.saveAndFlush(new Language(GERMAN_CODE, OTHER_NAME)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
