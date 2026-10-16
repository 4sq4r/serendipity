package com.serendipity.catalog.mapper;

import com.serendipity.catalog.domain.Country;
import com.serendipity.catalog.domain.Language;
import com.serendipity.catalog.domain.LanguageTest;
import com.serendipity.catalog.domain.University;
import com.serendipity.catalog.dto.CountryDto;
import com.serendipity.catalog.dto.LanguageDto;
import com.serendipity.catalog.dto.LanguageTestDto;
import com.serendipity.catalog.dto.UniversityDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CatalogMapper {

    CountryDto toDto(Country country);

    List<CountryDto> toCountryDtos(List<Country> countries);

    @Mapping(target = "countryId", source = "country.id")
    UniversityDto toDto(University university);

    List<UniversityDto> toUniversityDtos(List<University> universities);

    LanguageDto toDto(Language language);

    List<LanguageDto> toLanguageDtos(List<Language> languages);

    @Mapping(target = "languageId", source = "language.id")
    LanguageTestDto toDto(LanguageTest languageTest);

    List<LanguageTestDto> toLanguageTestDtos(List<LanguageTest> languageTests);
}
