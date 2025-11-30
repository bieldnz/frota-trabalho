package com.example.frota.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClienteNotFound(ClienteNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "CLIENTE_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EmailJaExisteException.class)
    public ResponseEntity<ErrorResponse> handleEmailJaExiste(EmailJaExisteException ex) {
        ErrorResponse error = new ErrorResponse(
            "EMAIL_ALREADY_EXISTS",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(TransportadoraNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransportadoraNotFound(TransportadoraNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "TRANSPORTADORA_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CnpjJaExisteException.class)
    public ResponseEntity<ErrorResponse> handleCnpjJaExiste(CnpjJaExisteException ex) {
        ErrorResponse error = new ErrorResponse(
            "CNPJ_ALREADY_EXISTS",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(TransporteNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransporteNotFound(TransporteNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "TRANSPORTE_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TransporteStatusException.class)
    public ResponseEntity<ErrorResponse> handleTransporteStatus(TransporteStatusException ex) {
        ErrorResponse error = new ErrorResponse(
            "INVALID_TRANSPORTE_STATUS",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CaixaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCaixaNotFound(CaixaNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "CAIXA_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CaixaIncompativelException.class)
    public ResponseEntity<ErrorResponse> handleCaixaIncompativel(CaixaIncompativelException ex) {
        ErrorResponse error = new ErrorResponse(
            "CAIXA_INCOMPATIVEL",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CaminhaoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCaminhaoNotFound(CaminhaoNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "CAMINHAO_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MarcaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMarcaNotFound(MarcaNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "MARCA_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MotoristaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMotoristaNotFound(MotoristaNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "MOTORISTA_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ViagemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleViagemNotFound(ViagemNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "VIAGEM_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ViagemStatusException.class)
    public ResponseEntity<ErrorResponse> handleViagemStatus(ViagemStatusException ex) {
        ErrorResponse error = new ErrorResponse(
            "INVALID_VIAGEM_STATUS",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ManutencaoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleManutencaoNotFound(ManutencaoNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "MANUTENCAO_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ManutencaoValidationException.class)
    public ResponseEntity<ErrorResponse> handleManutencaoValidation(ManutencaoValidationException ex) {
        ErrorResponse error = new ErrorResponse(
            "MANUTENCAO_VALIDATION_ERROR",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AvaliacaoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAvaliacaoNotFound(AvaliacaoNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "AVALIACAO_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AvaliacaoValidationException.class)
    public ResponseEntity<ErrorResponse> handleAvaliacaoValidation(AvaliacaoValidationException ex) {
        ErrorResponse error = new ErrorResponse(
            "AVALIACAO_VALIDATION_ERROR",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PlanejamentoException.class)
    public ResponseEntity<ErrorResponse> handlePlanejamento(PlanejamentoException ex) {
        ErrorResponse error = new ErrorResponse(
            "PLANEJAMENTO_ERROR",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(FreteCalculationException.class)
    public ResponseEntity<ErrorResponse> handleFreteCalculation(FreteCalculationException ex) {
        ErrorResponse error = new ErrorResponse(
            "FRETE_CALCULATION_ERROR",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(GoogleMapsApiException.class)
    public ResponseEntity<ErrorResponse> handleGoogleMapsApi(GoogleMapsApiException ex) {
        ErrorResponse error = new ErrorResponse(
            "GOOGLE_MAPS_API_ERROR",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "ENTITY_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
            "INVALID_ARGUMENT",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now());
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Validation Failed");
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            fieldErrors.put(error.getField(), error.getDefaultMessage()));
        
        errors.put("fieldErrors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(CpfJaExisteException.class)
    public ResponseEntity<ErrorResponse> handleCpfJaExiste(CpfJaExisteException ex) {
        ErrorResponse error = new ErrorResponse(
            "CPF_ALREADY_EXISTS",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(CnhJaExisteException.class)
    public ResponseEntity<ErrorResponse> handleCnhJaExiste(CnhJaExisteException ex) {
        ErrorResponse error = new ErrorResponse(
            "CNH_ALREADY_EXISTS",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MotoristaInativoException.class)
    public ResponseEntity<ErrorResponse> handleMotoristaInativo(MotoristaInativoException ex) {
        ErrorResponse error = new ErrorResponse(
            "MOTORISTA_INATIVO",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "Ocorreu um erro interno no servidor. Tente novamente mais tarde.",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}