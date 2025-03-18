package com.bugrunners.microsservicoa.errors.throwable;

public class NotFoundError extends RuntimeException {
    public NotFoundError() {
        super("Entity not found");
    }
}
