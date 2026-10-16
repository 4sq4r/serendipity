package com.serendipity.common.error;

public class ResourceNotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "%s with id %d not found";

    public ResourceNotFoundException(String resource, Long id) {
        super(MESSAGE_TEMPLATE.formatted(resource, id));
    }
}
