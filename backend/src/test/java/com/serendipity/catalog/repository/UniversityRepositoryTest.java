package com.serendipity.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.serendipity.RepositoryTest;
import com.serendipity.catalog.domain.Country;
import com.serendipity.catalog.domain.University;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

@RepositoryTest
class UniversityRepositoryTest {

    private static final String GERMANY_CODE = "DE";
    private static final String GERMANY_NAME = "Germany";
    private static final String FRANCE_CODE = "FR";
    private static final String FRANCE_NAME = "France";
    private static final String TUM_NAME = "Technical University of Munich";
    private static final String TUM_WEBSITE = "https://www.tum.de";
    private static final String TUM_DESCRIPTION = "Public research university in Munich";
    private static final String HEIDELBERG_NAME = "Heidelberg University";
    private static final String SORBONNE_NAME = "Sorbonne University";
    private static final String POLYTECHNIQUE_NAME = "École Polytechnique";
    private static final String NAME_PROPERTY = "name";
    private static final long UNKNOWN_COUNTRY_ID = -1L;
    private static final String INSERT_UNIVERSITY_SQL = "INSERT INTO universities (country_id, name) VALUES (?, ?)";
    private static final String DELETE_COUNTRY_SQL = "DELETE FROM countries WHERE id = ?";

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void savesAndLoadsUniversityWithCountry() {
        Country germany = countryRepository.saveAndFlush(new Country(GERMANY_CODE, GERMANY_NAME));
        Long id = universityRepository
                .saveAndFlush(new University(germany, TUM_NAME, TUM_WEBSITE, TUM_DESCRIPTION))
                .getId();
        entityManager.clear();

        University loaded = universityRepository.findById(id).orElseThrow();

        assertThat(loaded.getCountry().getId()).isEqualTo(germany.getId());
        assertThat(loaded.getName()).isEqualTo(TUM_NAME);
        assertThat(loaded.getWebsite()).isEqualTo(TUM_WEBSITE);
        assertThat(loaded.getDescription()).isEqualTo(TUM_DESCRIPTION);
    }

    @Test
    void rejectsUniversityWithUnknownCountry() {
        assertThatThrownBy(() -> jdbcTemplate.update(INSERT_UNIVERSITY_SQL, UNKNOWN_COUNTRY_ID, TUM_NAME))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void preventsDeletingCountryWithUniversities() {
        Country germany = countryRepository.saveAndFlush(new Country(GERMANY_CODE, GERMANY_NAME));
        universityRepository.saveAndFlush(new University(germany, TUM_NAME, TUM_WEBSITE, TUM_DESCRIPTION));

        assertThatThrownBy(() -> jdbcTemplate.update(DELETE_COUNTRY_SQL, germany.getId()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findsUniversitiesOfCountrySortedByName() {
        Country germany = countryRepository.save(new Country(GERMANY_CODE, GERMANY_NAME));
        Country france = countryRepository.save(new Country(FRANCE_CODE, FRANCE_NAME));
        universityRepository.save(new University(germany, TUM_NAME, null, null));
        universityRepository.save(new University(france, SORBONNE_NAME, null, null));
        universityRepository.save(new University(germany, HEIDELBERG_NAME, null, null));
        universityRepository.flush();

        List<University> universities = universityRepository.findAllByCountryId(germany.getId(), Sort.by(NAME_PROPERTY));

        assertThat(universities).extracting(University::getName).containsExactly(HEIDELBERG_NAME, TUM_NAME);
    }

    @Test
    void sortsNamesAlphabeticallyRegardlessOfAccents() {
        Country france = countryRepository.save(new Country(FRANCE_CODE, FRANCE_NAME));
        universityRepository.save(new University(france, SORBONNE_NAME, null, null));
        universityRepository.save(new University(france, POLYTECHNIQUE_NAME, null, null));
        universityRepository.flush();

        List<University> universities = universityRepository.findAllByCountryId(france.getId(), Sort.by(NAME_PROPERTY));

        assertThat(universities).extracting(University::getName).containsExactly(POLYTECHNIQUE_NAME, SORBONNE_NAME);
    }
}
