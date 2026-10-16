package com.serendipity.catalog.service;

import com.serendipity.catalog.dto.LanguageDto;
import com.serendipity.catalog.mapper.CatalogMapper;
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
    private final CatalogMapper catalogMapper;

    public LanguageService(LanguageRepository languageRepository, CatalogMapper catalogMapper) {
        this.languageRepository = languageRepository;
        this.catalogMapper = catalogMapper;
    }

    public List<LanguageDto> findAll() {
        return catalogMapper.toLanguageDtos(languageRepository.findAll(CatalogSort.BY_NAME));
    }

    public void ensureExists(Long id) {
        if (!languageRepository.existsById(id)) {
            throw new ResourceNotFoundException(RESOURCE, id);
        }
    }
}
