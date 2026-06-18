package com.lucasyohan.domain.Demo_Park.exceptions;

public class CpfUniqueViolationException extends RuntimeException {
    public CpfUniqueViolationException(String message) {
        super(message);
    }
}
