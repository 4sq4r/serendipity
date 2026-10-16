package com.serendipity.catalog.service;

import com.serendipity.catalog.domain.Country;
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

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    public Country getById(Long id) {
        return countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE, id));
    }
}
