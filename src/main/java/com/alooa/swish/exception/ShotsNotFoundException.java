package com.alooa.swish.exception;

public class ShotsNotFoundException extends RuntimeException {
    private final Long id;

    public ShotsNotFoundException(Long id) {
        super("Shots with id " + id + " not found");
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
