package com.victor.expenses_control.exception;

import com.victor.expenses_control.dto.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Método auxiliar para construir a resposta de erro

    private ErrorResponseDTO buildErrorResponse(HttpStatus status, String message, HttpServletRequest request) {
        return ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();
    }

    // Tratamento para EntityNotFoundException

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        ErrorResponseDTO response = buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    // Tratamento para MethodArgumentNotValidException

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        // Construir uma mensagem de erro detalhada para cada campo inválido
        String errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String field = ((FieldError) error).getField();
                    String defaultMessage = error.getDefaultMessage();
                    return field + ": " + defaultMessage;
                }).collect(Collectors.joining(", "));

        // Logar o erro de validação
        logger.warn("Erro de validação em {}: {}", request.getRequestURI(), errors);

        // Construir a resposta de erro
        ErrorResponseDTO response = buildErrorResponse(HttpStatus.BAD_REQUEST, errors, request);

        // Retornar a resposta com status 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Tratamento genérico para outras exceções
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Erro interno no servidor - Path: {} - Mensagem: {}", request.getRequestURI(), ex.getMessage(), ex);
        ErrorResponseDTO response = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // Tratamento para ExpenseNotFoundException
    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleExpenseNotFound(ExpenseNotFoundException ex, HttpServletRequest request) {
        logger.warn("Recurso não encontrado: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        ErrorResponseDTO response = buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
