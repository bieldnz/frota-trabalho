package com.example.frota.errors;

import java.time.LocalDateTime;

public record ErrorResponse(
    String errorCode,
    String message,
    LocalDateTime timestamp
) {}