package com.serendipity.catalog.repository;

import com.serendipity.catalog.domain.University;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {

    List<University> findAllByCountryId(Long countryId, Sort sort);
}
