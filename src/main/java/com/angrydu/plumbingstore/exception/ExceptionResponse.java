package com.angrydu.plumbingstore.exception;

public record ExceptionResponse(String message,
                                String typeException,
                                String exceptionDateTime) {
}
