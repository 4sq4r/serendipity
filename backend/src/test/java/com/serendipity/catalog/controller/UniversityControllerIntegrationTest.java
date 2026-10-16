package com.serendipity.catalog.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.serendipity.catalog.domain.Country;
import com.serendipity.catalog.domain.University;
import com.serendipity.catalog.dto.UniversityDto;
import com.serendipity.catalog.repository.CountryRepository;
import com.serendipity.catalog.repository.UniversityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UniversityControllerIntegrationTest extends CatalogIntegrationTest {

    private static final String COUNTRY_ID_PARAM = "countryId";
    private static final String GERMANY_CODE = "DE";
    private static final String GERMANY_NAME = "Germany";
    private static final String FRANCE_CODE = "FR";
    private static final String FRANCE_NAME = "France";
    private static final String TUM_NAME = "Technical University of Munich";
    private static final String TUM_WEBSITE = "https://www.tum.de";
    private static final String TUM_DESCRIPTION = "Public research university in Munich";
    private static final String SORBONNE_NAME = "Sorbonne University";
    private static final String HEIDELBERG_NAME = "Heidelberg University";
    private static final String UNKNOWN_COUNTRY_DETAIL = "Country with id %d not found".formatted(UNKNOWN_ID);

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private UniversityRepository universityRepository;

    private Country germany;
    private University tum;
    private University sorbonne;
    private University heidelberg;

    @BeforeEach
    void createUniversities() {
        germany = countryRepository.save(new Country(GERMANY_CODE, GERMANY_NAME));
        Country france = countryRepository.save(new Country(FRANCE_CODE, FRANCE_NAME));
        tum = universityRepository.save(new University(germany, TUM_NAME, TUM_WEBSITE, TUM_DESCRIPTION));
        sorbonne = universityRepository.save(new University(france, SORBONNE_NAME, null, null));
        heidelberg = universityRepository.save(new University(germany, HEIDELBERG_NAME, null, null));
    }

    @Test
    void returnsAllUniversitiesSortedByNameWithoutFilter() {
        ResponseEntity<UniversityDto[]> response =
                restTemplate.getForEntity(url(CatalogApi.UNIVERSITIES_PATH), UniversityDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(toDto(heidelberg), toDto(sorbonne), toDto(tum));
    }

    @Test
    void returnsOnlyUniversitiesOfRequestedCountry() {
        ResponseEntity<UniversityDto[]> response = restTemplate.getForEntity(
                url(CatalogApi.UNIVERSITIES_PATH, COUNTRY_ID_PARAM, germany.getId()), UniversityDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(toDto(heidelberg), toDto(tum));
    }

    @Test
    void returnsNotFoundForUnknownCountry() {
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(
                url(CatalogApi.UNIVERSITIES_PATH, COUNTRY_ID_PARAM, UNKNOWN_ID), JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().get(DETAIL_PROPERTY).asText()).isEqualTo(UNKNOWN_COUNTRY_DETAIL);
    }

    @Test
    void rejectsNonPositiveCountryId() {
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(
                url(CatalogApi.UNIVERSITIES_PATH, COUNTRY_ID_PARAM, NON_POSITIVE_ID), JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().get(ERRORS_FIELD).get(0).get(FIELD_PROPERTY).asText())
                .isEqualTo(COUNTRY_ID_PARAM);
    }

    private static UniversityDto toDto(University university) {
        return new UniversityDto(
                university.getId(),
                university.getCountry().getId(),
                university.getName(),
                university.getWebsite(),
                university.getDescription());
    }
}
