package com.serendipity.catalog.repository;

import com.serendipity.catalog.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
