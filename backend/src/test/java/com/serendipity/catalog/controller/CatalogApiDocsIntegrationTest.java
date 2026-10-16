package com.serendipity.catalog.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class CatalogApiDocsIntegrationTest extends CatalogIntegrationTest {

    private static final String API_DOCS_PATH = "/v3/api-docs";
    private static final String PATHS_PROPERTY = "paths";

    @Test
    void catalogEndpointsArePublishedInOpenApi() {
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(API_DOCS_PATH, JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode paths = response.getBody().get(PATHS_PROPERTY);
        assertThat(paths.has(url(CatalogApi.COUNTRIES_PATH))).isTrue();
        assertThat(paths.has(url(CatalogApi.UNIVERSITIES_PATH))).isTrue();
        assertThat(paths.has(url(CatalogApi.LANGUAGES_PATH))).isTrue();
        assertThat(paths.has(url(CatalogApi.LANGUAGE_TESTS_PATH))).isTrue();
    }
}
