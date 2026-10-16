package com.serendipity.catalog.service;

import com.serendipity.catalog.dto.CountryDto;
import com.serendipity.catalog.mapper.CatalogMapper;
import com.serendipity.catalog.repository.CountryRepository;
import com.serendipity.common.error.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CountryService {

    private static final String RESOURCE = "Country";

    private final CountryRepository countryRepository;
    private final CatalogMapper catalogMapper;

    public CountryService(CountryRepository countryRepository, CatalogMapper catalogMapper) {
        this.countryRepository = countryRepository;
        this.catalogMapper = catalogMapper;
    }

    public List<CountryDto> findAll() {
        return catalogMapper.toCountryDtos(countryRepository.findAll());
    }

    public void ensureExists(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new ResourceNotFoundException(RESOURCE, id);
        }
    }
}
