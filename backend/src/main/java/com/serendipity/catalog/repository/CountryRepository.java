package com.serendipity.catalog.repository;

import com.serendipity.catalog.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
