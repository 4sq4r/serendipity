package com.serendipity.catalog.controller;

import com.serendipity.catalog.dto.CountryDto;
import com.serendipity.catalog.service.CountryService;
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
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping(CatalogApi.COUNTRIES_PATH)
    @Operation(summary = "List countries", description = CatalogApi.COUNTRIES_ORDER_DESCRIPTION)
    public List<CountryDto> listCountries() {
        return countryService.findAll();
    }
}
