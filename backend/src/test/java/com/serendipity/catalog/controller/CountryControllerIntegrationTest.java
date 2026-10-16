package com.serendipity.catalog.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.serendipity.catalog.domain.Country;
import com.serendipity.catalog.dto.CountryDto;
import com.serendipity.catalog.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class CountryControllerIntegrationTest extends CatalogIntegrationTest {

    private static final String GERMANY_CODE = "DE";
    private static final String GERMANY_NAME = "Germany";

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void returnsCountries() {
        Country germany = countryRepository.save(new Country(GERMANY_CODE, GERMANY_NAME));

        ResponseEntity<CountryDto[]> response =
                restTemplate.getForEntity(url(CatalogApi.COUNTRIES_PATH), CountryDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(new CountryDto(germany.getId(), GERMANY_CODE, GERMANY_NAME));
    }
}
