package com.serendipity.catalog.service;

import com.serendipity.catalog.domain.University;
import com.serendipity.catalog.dto.UniversityDto;
import com.serendipity.catalog.mapper.CatalogMapper;
import com.serendipity.catalog.repository.UniversityRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final CountryService countryService;
    private final CatalogMapper catalogMapper;

    public UniversityService(
            UniversityRepository universityRepository,
            CountryService countryService,
            CatalogMapper catalogMapper) {
        this.universityRepository = universityRepository;
        this.countryService = countryService;
        this.catalogMapper = catalogMapper;
    }

    public List<UniversityDto> findAll(Long countryId) {
        return catalogMapper.toUniversityDtos(findUniversities(countryId));
    }

    private List<University> findUniversities(Long countryId) {
        if (countryId == null) {
            return universityRepository.findAll(CatalogSort.BY_NAME);
        }
        countryService.ensureExists(countryId);
        return universityRepository.findAllByCountryId(countryId, CatalogSort.BY_NAME);
    }
}
