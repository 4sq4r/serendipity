package com.serendipity.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.serendipity.RepositoryTest;
import com.serendipity.catalog.domain.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

@RepositoryTest
class CountryRepositoryTest {

    private static final String GERMANY_CODE = "DE";
    private static final String GERMANY_NAME = "Germany";
    private static final String OTHER_NAME = "Deutschland";

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void savesAndLoadsCountry() {
        Long id = countryRepository.saveAndFlush(new Country(GERMANY_CODE, GERMANY_NAME)).getId();
        entityManager.clear();

        Country loaded = countryRepository.findById(id).orElseThrow();

        assertThat(loaded.getCode()).isEqualTo(GERMANY_CODE);
        assertThat(loaded.getName()).isEqualTo(GERMANY_NAME);
    }

    @Test
    void rejectsDuplicateCountryCode() {
        countryRepository.saveAndFlush(new Country(GERMANY_CODE, GERMANY_NAME));

        assertThatThrownBy(() -> countryRepository.saveAndFlush(new Country(GERMANY_CODE, OTHER_NAME)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
