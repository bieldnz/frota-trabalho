package com.example.frota.errors;

public class GoogleMapsApiException extends RuntimeException {
    public GoogleMapsApiException(String message) {
        super(message);
    }

    public GoogleMapsApiException(String message, Throwable cause) {
        super(message, cause);
    }
}