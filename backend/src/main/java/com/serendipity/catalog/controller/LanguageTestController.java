package com.serendipity.catalog.controller;

import com.serendipity.catalog.dto.LanguageTestDto;
import com.serendipity.catalog.service.LanguageTestService;
import com.serendipity.common.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.V1)
@Tag(name = CatalogApi.TAG)
public class LanguageTestController {

    private final LanguageTestService languageTestService;

    public LanguageTestController(LanguageTestService languageTestService) {
        this.languageTestService = languageTestService;
    }

    @GetMapping(CatalogApi.LANGUAGE_TESTS_PATH)
    @Operation(
            summary = "List language tests",
            description = "Language tests are sorted by name in ascending order. "
                    + "Responds with 404 when the requested language does not exist.")
    public List<LanguageTestDto> listLanguageTests(
            @Parameter(description = "Return only tests of this language")
            @RequestParam(required = false) @Positive Long languageId) {
        return languageTestService.findAll(languageId);
    }
}
