package com.serendipity.catalog.service;

import org.springframework.data.domain.Sort;

final class CatalogSort {

    static final String NAME_PROPERTY = "name";
    static final Sort BY_NAME = Sort.by(NAME_PROPERTY);

    private CatalogSort() {
    }
}
