package com.angrydu.plumbingstore.exception;

public class CustomException extends RuntimeException {
    private final ExceptionLocations location;

    public CustomException(String message, ExceptionLocations location) {
        super(message);
        this.location = location;
    }

    public ExceptionLocations getLocation() {
        return location;
    }
}
