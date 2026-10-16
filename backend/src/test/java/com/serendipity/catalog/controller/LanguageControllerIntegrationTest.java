package com.serendipity.catalog.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.serendipity.catalog.domain.Language;
import com.serendipity.catalog.dto.LanguageDto;
import com.serendipity.catalog.repository.LanguageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class LanguageControllerIntegrationTest extends CatalogIntegrationTest {

    private static final String GERMAN_CODE = "de";
    private static final String GERMAN_NAME = "German";
    private static final String ENGLISH_CODE = "en";
    private static final String ENGLISH_NAME = "English";

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    void returnsLanguagesSortedByName() {
        Language german = languageRepository.save(new Language(GERMAN_CODE, GERMAN_NAME));
        Language english = languageRepository.save(new Language(ENGLISH_CODE, ENGLISH_NAME));

        ResponseEntity<LanguageDto[]> response =
                restTemplate.getForEntity(url(CatalogApi.LANGUAGES_PATH), LanguageDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(
                new LanguageDto(english.getId(), ENGLISH_CODE, ENGLISH_NAME),
                new LanguageDto(german.getId(), GERMAN_CODE, GERMAN_NAME));
    }
}
