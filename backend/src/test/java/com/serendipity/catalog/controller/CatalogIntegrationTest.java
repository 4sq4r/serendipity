package com.serendipity.catalog.controller;

import com.serendipity.TestcontainersConfiguration;
import com.serendipity.common.web.ApiPaths;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
abstract class CatalogIntegrationTest {

    protected static final String ERRORS_FIELD = "errors";
    protected static final String FIELD_PROPERTY = "field";
    protected static final String DETAIL_PROPERTY = "detail";
    protected static final long UNKNOWN_ID = Long.MAX_VALUE;
    protected static final long NON_POSITIVE_ID = 0L;

    private static final String TRUNCATE_CATALOG_SQL =
            "TRUNCATE TABLE university, country, language_test, language RESTART IDENTITY";

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanCatalog() {
        jdbcTemplate.execute(TRUNCATE_CATALOG_SQL);
    }

    protected static String url(String path) {
        return ApiPaths.V1 + path;
    }

    protected static String url(String path, String parameter, Object value) {
        return UriComponentsBuilder.fromPath(url(path)).queryParam(parameter, value).toUriString();
    }
}
