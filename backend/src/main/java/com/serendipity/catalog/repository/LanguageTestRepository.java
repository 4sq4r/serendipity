package com.serendipity.catalog.repository;

import com.serendipity.catalog.domain.LanguageTest;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageTestRepository extends JpaRepository<LanguageTest, Long> {

    List<LanguageTest> findAllByLanguageId(Long languageId, Sort sort);
}
