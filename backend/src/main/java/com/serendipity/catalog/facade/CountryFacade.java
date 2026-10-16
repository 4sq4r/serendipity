package com.serendipity.catalog.facade;

import com.serendipity.catalog.dto.CountryDto;
import com.serendipity.catalog.mapper.CatalogMapper;
import com.serendipity.catalog.service.CountryService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CountryFacade {

    private final CountryService countryService;
    private final CatalogMapper catalogMapper;

    public CountryFacade(CountryService countryService, CatalogMapper catalogMapper) {
        this.countryService = countryService;
        this.catalogMapper = catalogMapper;
    }

    public List<CountryDto> findAll() {
        return catalogMapper.toCountryDtos(countryService.findAll());
    }
}
