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
    private static final String AUSTRIA_CODE = "AT";
    private static final String AUSTRIA_NAME = "Austria";
    private static final String ICELAND_CODE = "IS";
    private static final String ICELAND_NAME = "Ísland";
    private static final String JAPAN_CODE = "JP";
    private static final String JAPAN_NAME = "Japan";

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

    @Test
    void returnsCountriesSortedByNameAscending() {
        Country japan = countryRepository.save(new Country(JAPAN_CODE, JAPAN_NAME));
        Country austria = countryRepository.save(new Country(AUSTRIA_CODE, AUSTRIA_NAME));
        Country iceland = countryRepository.save(new Country(ICELAND_CODE, ICELAND_NAME));
        Country germany = countryRepository.save(new Country(GERMANY_CODE, GERMANY_NAME));

        ResponseEntity<CountryDto[]> response =
                restTemplate.getForEntity(url(CatalogApi.COUNTRIES_PATH), CountryDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(
                new CountryDto(austria.getId(), AUSTRIA_CODE, AUSTRIA_NAME),
                new CountryDto(germany.getId(), GERMANY_CODE, GERMANY_NAME),
                new CountryDto(iceland.getId(), ICELAND_CODE, ICELAND_NAME),
                new CountryDto(japan.getId(), JAPAN_CODE, JAPAN_NAME));
    }
}
