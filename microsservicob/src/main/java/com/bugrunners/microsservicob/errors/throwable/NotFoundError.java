package com.bugrunners.microsservicob.errors.throwable;

public class NotFoundError extends RuntimeException {
    public NotFoundError() {
        super("Entity not found");
    }
}
