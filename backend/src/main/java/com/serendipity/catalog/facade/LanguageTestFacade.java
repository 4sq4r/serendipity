package com.serendipity.catalog.facade;

import com.serendipity.catalog.dto.LanguageTestDto;
import com.serendipity.catalog.mapper.CatalogMapper;
import com.serendipity.catalog.processor.LanguageTestProcessor;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LanguageTestFacade {

    private final LanguageTestProcessor languageTestProcessor;
    private final CatalogMapper catalogMapper;

    public LanguageTestFacade(LanguageTestProcessor languageTestProcessor, CatalogMapper catalogMapper) {
        this.languageTestProcessor = languageTestProcessor;
        this.catalogMapper = catalogMapper;
    }

    public List<LanguageTestDto> findAll(Long languageId) {
        return catalogMapper.toLanguageTestDtos(languageTestProcessor.findLanguageTests(languageId));
    }
}
