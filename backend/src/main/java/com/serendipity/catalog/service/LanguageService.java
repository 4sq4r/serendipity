package com.serendipity.catalog.service;

import com.serendipity.catalog.domain.Language;
import com.serendipity.catalog.repository.LanguageRepository;
import com.serendipity.common.error.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LanguageService {

    private static final String RESOURCE = "Language";

    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public List<Language> findAll() {
        return languageRepository.findAll(CatalogSort.BY_NAME);
    }

    public Language getById(Long id) {
        return languageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE, id));
    }
}
