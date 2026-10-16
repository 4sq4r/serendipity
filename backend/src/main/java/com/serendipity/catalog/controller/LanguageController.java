package com.serendipity.catalog.controller;

import com.serendipity.catalog.dto.LanguageDto;
import com.serendipity.catalog.service.LanguageService;
import com.serendipity.common.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.V1)
@Tag(name = CatalogApi.TAG)
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping(CatalogApi.LANGUAGES_PATH)
    @Operation(summary = "List languages", description = "Languages are sorted by name in ascending order.")
    public List<LanguageDto> listLanguages() {
        return languageService.findAll();
    }
}
