package com.serendipity.catalog.facade;

import com.serendipity.catalog.dto.LanguageDto;
import com.serendipity.catalog.mapper.CatalogMapper;
import com.serendipity.catalog.service.LanguageService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LanguageFacade {

    private final LanguageService languageService;
    private final CatalogMapper catalogMapper;

    public LanguageFacade(LanguageService languageService, CatalogMapper catalogMapper) {
        this.languageService = languageService;
        this.catalogMapper = catalogMapper;
    }

    public List<LanguageDto> findAll() {
        return catalogMapper.toLanguageDtos(languageService.findAll());
    }
}
