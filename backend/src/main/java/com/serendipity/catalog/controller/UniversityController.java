package com.serendipity.catalog.controller;

import com.serendipity.catalog.dto.UniversityDto;
import com.serendipity.catalog.service.UniversityService;
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
public class UniversityController {

    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping(CatalogApi.UNIVERSITIES_PATH)
    @Operation(
            summary = "List universities",
            description = "Universities are sorted by name in ascending order. "
                    + "Responds with 404 when the requested country does not exist.")
    public List<UniversityDto> listUniversities(
            @Parameter(description = "Return only universities of this country")
            @RequestParam(required = false) @Positive Long countryId) {
        return universityService.findAll(countryId);
    }
}
