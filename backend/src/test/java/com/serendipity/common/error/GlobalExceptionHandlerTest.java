package com.serendipity.common.error;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

class GlobalExceptionHandlerTest {

    private static final String NOT_FOUND_PATH = "/test/not-found";
    private static final String BODY_PATH = "/test/body";
    private static final String PARAM_PATH = "/test/param";
    private static final String FAILURE_PATH = "/test/failure";
    private static final String RESOURCE = "Thing";
    private static final long MISSING_ID = 42L;
    private static final String NOT_FOUND_DETAIL = "Thing with id 42 not found";
    private static final String VALUE_PARAM = "value";
    private static final String NON_POSITIVE_VALUE = "0";
    private static final String NON_NUMERIC_VALUE = "abc";
    private static final String NAME_FIELD = "name";
    private static final String BLANK_NAME_BODY = "{\"name\":\"\"}";
    private static final String SECRET_MESSAGE = "database password is hunter2";
    private static final String TYPE_MISMATCH_MESSAGE = "must be a valid Long";

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FailingController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void resourceNotFoundReturns404Problem() throws Exception {
        mockMvc.perform(get(NOT_FOUND_PATH))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value(NOT_FOUND_DETAIL))
                .andExpect(jsonPath("$.instance").value(NOT_FOUND_PATH));
    }

    @Test
    void invalidRequestBodyReturns400WithFieldList() throws Exception {
        mockMvc.perform(post(BODY_PATH).contentType(MediaType.APPLICATION_JSON).content(BLANK_NAME_BODY))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(GlobalExceptionHandler.VALIDATION_FAILED_DETAIL))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value(NAME_FIELD))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());
    }

    @Test
    void invalidRequestParameterReturns400WithFieldList() throws Exception {
        mockMvc.perform(get(PARAM_PATH).param(VALUE_PARAM, NON_POSITIVE_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(GlobalExceptionHandler.VALIDATION_FAILED_DETAIL))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value(VALUE_PARAM))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());
    }

    @Test
    void mistypedRequestParameterReturns400WithFieldList() throws Exception {
        mockMvc.perform(get(PARAM_PATH).param(VALUE_PARAM, NON_NUMERIC_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(GlobalExceptionHandler.VALIDATION_FAILED_DETAIL))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value(VALUE_PARAM))
                .andExpect(jsonPath("$.errors[0].message").value(TYPE_MISMATCH_MESSAGE));
    }

    @Test
    void unexpectedExceptionReturns500WithoutDetails() throws Exception {
        mockMvc.perform(get(FAILURE_PATH))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.detail").value(GlobalExceptionHandler.INTERNAL_ERROR_DETAIL))
                .andExpect(content().string(not(containsString(SECRET_MESSAGE))));
    }

    @RestController
    static class FailingController {

        @GetMapping(NOT_FOUND_PATH)
        String notFound() {
            throw new ResourceNotFoundException(RESOURCE, MISSING_ID);
        }

        @PostMapping(BODY_PATH)
        String body(@Valid @RequestBody NamedRequest request) {
            return request.name();
        }

        @GetMapping(PARAM_PATH)
        Long param(@RequestParam @Positive Long value) {
            return value;
        }

        @GetMapping(FAILURE_PATH)
        String failure() {
            throw new IllegalStateException(SECRET_MESSAGE);
        }
    }

    record NamedRequest(@NotBlank String name) {
    }
}
