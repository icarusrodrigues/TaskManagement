package com.task.management.api.exceptions;

public class AlreadyStartedTaskException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public AlreadyStartedTaskException() {
        super("Task already started");
    }
}
