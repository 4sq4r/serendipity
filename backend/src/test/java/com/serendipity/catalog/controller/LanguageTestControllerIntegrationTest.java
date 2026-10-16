package com.serendipity.catalog.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.serendipity.catalog.domain.Language;
import com.serendipity.catalog.domain.LanguageTest;
import com.serendipity.catalog.dto.LanguageTestDto;
import com.serendipity.catalog.repository.LanguageRepository;
import com.serendipity.catalog.repository.LanguageTestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class LanguageTestControllerIntegrationTest extends CatalogIntegrationTest {

    private static final String LANGUAGE_ID_PARAM = "languageId";
    private static final String ENGLISH_CODE = "en";
    private static final String ENGLISH_NAME = "English";
    private static final String GERMAN_CODE = "de";
    private static final String GERMAN_NAME = "German";
    private static final String TOEFL_NAME = "TOEFL";
    private static final String TOEFL_DESCRIPTION = "Test of English as a Foreign Language";
    private static final String TESTDAF_NAME = "TestDaF";
    private static final String IELTS_NAME = "IELTS";
    private static final String UNKNOWN_LANGUAGE_DETAIL = "Language with id %d not found".formatted(UNKNOWN_ID);

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageTestRepository languageTestRepository;

    private Language english;
    private LanguageTest toefl;
    private LanguageTest testDaf;
    private LanguageTest ielts;

    @BeforeEach
    void createLanguageTests() {
        english = languageRepository.save(new Language(ENGLISH_CODE, ENGLISH_NAME));
        Language german = languageRepository.save(new Language(GERMAN_CODE, GERMAN_NAME));
        toefl = languageTestRepository.save(new LanguageTest(english, TOEFL_NAME, TOEFL_DESCRIPTION));
        testDaf = languageTestRepository.save(new LanguageTest(german, TESTDAF_NAME, null));
        ielts = languageTestRepository.save(new LanguageTest(english, IELTS_NAME, null));
    }

    @Test
    void returnsAllLanguageTestsSortedByNameWithoutFilter() {
        ResponseEntity<LanguageTestDto[]> response =
                restTemplate.getForEntity(url(CatalogApi.LANGUAGE_TESTS_PATH), LanguageTestDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(toDto(ielts), toDto(testDaf), toDto(toefl));
    }

    @Test
    void returnsOnlyLanguageTestsOfRequestedLanguage() {
        ResponseEntity<LanguageTestDto[]> response = restTemplate.getForEntity(
                url(CatalogApi.LANGUAGE_TESTS_PATH, LANGUAGE_ID_PARAM, english.getId()), LanguageTestDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(toDto(ielts), toDto(toefl));
    }

    @Test
    void returnsNotFoundForUnknownLanguage() {
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(
                url(CatalogApi.LANGUAGE_TESTS_PATH, LANGUAGE_ID_PARAM, UNKNOWN_ID), JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().get(DETAIL_PROPERTY).asText()).isEqualTo(UNKNOWN_LANGUAGE_DETAIL);
    }

    @Test
    void rejectsNonPositiveLanguageId() {
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(
                url(CatalogApi.LANGUAGE_TESTS_PATH, LANGUAGE_ID_PARAM, NON_POSITIVE_ID), JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().get(ERRORS_FIELD).get(0).get(FIELD_PROPERTY).asText())
                .isEqualTo(LANGUAGE_ID_PARAM);
    }

    private static LanguageTestDto toDto(LanguageTest languageTest) {
        return new LanguageTestDto(
                languageTest.getId(),
                languageTest.getLanguage().getId(),
                languageTest.getName(),
                languageTest.getDescription());
    }
}
