package com.serendipity.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.serendipity.RepositoryTest;
import com.serendipity.catalog.domain.Language;
import com.serendipity.catalog.domain.LanguageTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

@RepositoryTest
class LanguageTestRepositoryTest {

    private static final String ENGLISH_CODE = "en";
    private static final String ENGLISH_NAME = "English";
    private static final String GERMAN_CODE = "de";
    private static final String GERMAN_NAME = "German";
    private static final String IELTS_NAME = "IELTS";
    private static final String IELTS_DESCRIPTION = "International English Language Testing System";
    private static final String TOEFL_NAME = "TOEFL";
    private static final String TESTDAF_NAME = "TestDaF";
    private static final String NAME_PROPERTY = "name";
    private static final long UNKNOWN_LANGUAGE_ID = -1L;
    private static final String INSERT_LANGUAGE_TEST_SQL = "INSERT INTO language_test (language_id, name) VALUES (?, ?)";
    private static final String DELETE_LANGUAGE_SQL = "DELETE FROM language WHERE id = ?";

    @Autowired
    private LanguageTestRepository languageTestRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void savesAndLoadsLanguageTestWithLanguage() {
        Language english = languageRepository.saveAndFlush(new Language(ENGLISH_CODE, ENGLISH_NAME));
        Long id = languageTestRepository
                .saveAndFlush(new LanguageTest(english, IELTS_NAME, IELTS_DESCRIPTION))
                .getId();
        entityManager.clear();

        LanguageTest loaded = languageTestRepository.findById(id).orElseThrow();

        assertThat(loaded.getLanguage().getId()).isEqualTo(english.getId());
        assertThat(loaded.getName()).isEqualTo(IELTS_NAME);
        assertThat(loaded.getDescription()).isEqualTo(IELTS_DESCRIPTION);
    }

    @Test
    void rejectsLanguageTestWithUnknownLanguage() {
        assertThatThrownBy(() -> jdbcTemplate.update(INSERT_LANGUAGE_TEST_SQL, UNKNOWN_LANGUAGE_ID, IELTS_NAME))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void preventsDeletingLanguageWithLanguageTests() {
        Language english = languageRepository.saveAndFlush(new Language(ENGLISH_CODE, ENGLISH_NAME));
        languageTestRepository.saveAndFlush(new LanguageTest(english, IELTS_NAME, IELTS_DESCRIPTION));

        assertThatThrownBy(() -> jdbcTemplate.update(DELETE_LANGUAGE_SQL, english.getId()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findsLanguageTestsOfLanguageSortedByName() {
        Language english = languageRepository.save(new Language(ENGLISH_CODE, ENGLISH_NAME));
        Language german = languageRepository.save(new Language(GERMAN_CODE, GERMAN_NAME));
        languageTestRepository.save(new LanguageTest(english, TOEFL_NAME, null));
        languageTestRepository.save(new LanguageTest(german, TESTDAF_NAME, null));
        languageTestRepository.save(new LanguageTest(english, IELTS_NAME, null));
        languageTestRepository.flush();

        List<LanguageTest> tests = languageTestRepository.findAllByLanguageId(english.getId(), Sort.by(NAME_PROPERTY));

        assertThat(tests).extracting(LanguageTest::getName).containsExactly(IELTS_NAME, TOEFL_NAME);
    }
}
