package com.task.management.api.exceptions;

public class AlreadyFinishedTaskException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public AlreadyFinishedTaskException() {
        super("Already finished task");
    }
}
