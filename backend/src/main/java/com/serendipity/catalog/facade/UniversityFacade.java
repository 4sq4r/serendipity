package com.serendipity.catalog.facade;

import com.serendipity.catalog.dto.UniversityDto;
import com.serendipity.catalog.mapper.CatalogMapper;
import com.serendipity.catalog.processor.UniversityProcessor;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UniversityFacade {

    private final UniversityProcessor universityProcessor;
    private final CatalogMapper catalogMapper;

    public UniversityFacade(UniversityProcessor universityProcessor, CatalogMapper catalogMapper) {
        this.universityProcessor = universityProcessor;
        this.catalogMapper = catalogMapper;
    }

    public List<UniversityDto> findAll(Long countryId) {
        return catalogMapper.toUniversityDtos(universityProcessor.findUniversities(countryId));
    }
}
