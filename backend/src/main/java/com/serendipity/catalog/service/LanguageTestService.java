package com.serendipity.catalog.service;

import com.serendipity.catalog.domain.LanguageTest;
import com.serendipity.catalog.dto.LanguageTestDto;
import com.serendipity.catalog.mapper.CatalogMapper;
import com.serendipity.catalog.repository.LanguageTestRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LanguageTestService {

    private final LanguageTestRepository languageTestRepository;
    private final LanguageService languageService;
    private final CatalogMapper catalogMapper;

    public LanguageTestService(
            LanguageTestRepository languageTestRepository,
            LanguageService languageService,
            CatalogMapper catalogMapper) {
        this.languageTestRepository = languageTestRepository;
        this.languageService = languageService;
        this.catalogMapper = catalogMapper;
    }

    public List<LanguageTestDto> findAll(Long languageId) {
        return catalogMapper.toLanguageTestDtos(findLanguageTests(languageId));
    }

    private List<LanguageTest> findLanguageTests(Long languageId) {
        if (languageId == null) {
            return languageTestRepository.findAll(CatalogSort.BY_NAME);
        }
        languageService.ensureExists(languageId);
        return languageTestRepository.findAllByLanguageId(languageId, CatalogSort.BY_NAME);
    }
}
