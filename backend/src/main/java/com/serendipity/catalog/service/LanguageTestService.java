package com.serendipity.catalog.service;

import com.serendipity.catalog.domain.LanguageTest;
import com.serendipity.catalog.repository.LanguageTestRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LanguageTestService {

    private final LanguageTestRepository languageTestRepository;

    public LanguageTestService(LanguageTestRepository languageTestRepository) {
        this.languageTestRepository = languageTestRepository;
    }

    public List<LanguageTest> findAll() {
        return languageTestRepository.findAll(CatalogSort.BY_NAME);
    }

    public List<LanguageTest> findAllByLanguageId(Long languageId) {
        return languageTestRepository.findAllByLanguageId(languageId, CatalogSort.BY_NAME);
    }
}
