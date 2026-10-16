package com.serendipity.catalog.processor;

import com.serendipity.catalog.domain.Language;
import com.serendipity.catalog.domain.LanguageTest;
import com.serendipity.catalog.service.LanguageService;
import com.serendipity.catalog.service.LanguageTestService;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class LanguageTestProcessor {

    private final LanguageTestService languageTestService;
    private final LanguageService languageService;

    public LanguageTestProcessor(LanguageTestService languageTestService, LanguageService languageService) {
        this.languageTestService = languageTestService;
        this.languageService = languageService;
    }

    public List<LanguageTest> findLanguageTests(Long languageId) {
        if (languageId == null) {
            return languageTestService.findAll();
        }
        Language language = languageService.getById(languageId);
        return languageTestService.findAllByLanguageId(language.getId());
    }
}
