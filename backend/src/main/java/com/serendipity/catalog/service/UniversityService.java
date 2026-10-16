package com.serendipity.catalog.service;

import com.serendipity.catalog.domain.University;
import com.serendipity.catalog.repository.UniversityRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UniversityService {

    private final UniversityRepository universityRepository;

    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public List<University> findAll() {
        return universityRepository.findAll(CatalogSort.BY_NAME);
    }

    public List<University> findAllByCountryId(Long countryId) {
        return universityRepository.findAllByCountryId(countryId, CatalogSort.BY_NAME);
    }
}
