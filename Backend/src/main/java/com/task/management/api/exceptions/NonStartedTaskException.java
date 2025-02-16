package com.task.management.api.exceptions;

public class NonStartedTaskException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public NonStartedTaskException() {
        super("Non started task");
    }
}
