package com.serendipity.catalog.processor;

import com.serendipity.catalog.domain.Country;
import com.serendipity.catalog.domain.University;
import com.serendipity.catalog.service.CountryService;
import com.serendipity.catalog.service.UniversityService;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class UniversityProcessor {

    private final UniversityService universityService;
    private final CountryService countryService;

    public UniversityProcessor(UniversityService universityService, CountryService countryService) {
        this.universityService = universityService;
        this.countryService = countryService;
    }

    public List<University> findUniversities(Long countryId) {
        if (countryId == null) {
            return universityService.findAll();
        }
        Country country = countryService.getById(countryId);
        return universityService.findAllByCountryId(country.getId());
    }
}
