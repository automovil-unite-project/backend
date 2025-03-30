package com.vision_rent.automovil_unite.presentation.advice;

import com.vision_rent.automovil_unite.application.exception.EmailAlreadyExistsException;
import com.vision_rent.automovil_unite.application.exception.InvalidCredentialsException;
import com.vision_rent.automovil_unite.application.exception.InvalidOperationException;
import com.vision_rent.automovil_unite.application.exception.ResourceNotFoundException;
import com.vision_rent.automovil_unite.application.exception.UnauthorizedOperationException;
import com.vision_rent.automovil_unite.domain.exception.DomainException;
import com.vision_rent.automovil_unite.domain.exception.InvalidRentalOperationException;
import com.vision_rent.automovil_unite.domain.exception.UserBannedException;
import com.vision_rent.automovil_unite.domain.exception.VehicleNotAvailableException;
import com.vision_rent.automovil_unite.infrastructure.storage.StorageException;
import com.vision_rent.automovil_unite.infrastructure.storage.StorageFileNotFoundException;
import com.vision_rent.automovil_unite.presentation.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador global para manejar excepciones.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de recursos no encontrados.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }
    
    /**
     * Maneja excepciones de email ya existente.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
    }
    
    /**
     * Maneja excepciones de credenciales inválidas.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentialsException(
            InvalidCredentialsException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request);
    }
    
    /**
     * Maneja excepciones de operaciones inválidas.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidOperationException(
            InvalidOperationException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }
    
    /**
     * Maneja excepciones de operaciones no autorizadas.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorizedOperationException(
            UnauthorizedOperationException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN, request);
    }
    
    /**
     * Maneja excepciones de acceso denegado.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        return createErrorResponse("Acceso denegado: No tiene permisos para realizar esta operación", 
                HttpStatus.FORBIDDEN, request);
    }
    
    /**
     * Maneja excepciones de validación de argumentos.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Maneja excepciones del dominio.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomainException(
            DomainException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }
    
    /**
     * Maneja excepciones de operaciones de alquiler inválidas.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(InvalidRentalOperationException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRentalOperationException(
            InvalidRentalOperationException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }
    
    /**
     * Maneja excepciones de usuario baneado.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(UserBannedException.class)
    public ResponseEntity<ApiErrorResponse> handleUserBannedException(
            UserBannedException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN, request);
    }
    
    /**
     * Maneja excepciones de vehículo no disponible.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(VehicleNotAvailableException.class)
    public ResponseEntity<ApiErrorResponse> handleVehicleNotAvailableException(
            VehicleNotAvailableException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
    }
    
    /**
     * Maneja excepciones de almacenamiento.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ApiErrorResponse> handleStorageException(
            StorageException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    
    /**
     * Maneja excepciones de archivo no encontrado.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleStorageFileNotFoundException(
            StorageFileNotFoundException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }
    
    /**
     * Maneja otras excepciones no capturadas.
     *
     * @param ex Excepción
     * @param request Solicitud web
     * @return Respuesta de error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        return createErrorResponse("Se produjo un error interno en el servidor", 
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    
    /**
     * Crea una respuesta de error.
     *
     * @param message Mensaje de error
     * @param status Estado HTTP
     * @param request Solicitud web
     * @return Respuesta de error
     */
    private ResponseEntity<ApiErrorResponse> createErrorResponse(
            String message, HttpStatus status, WebRequest request) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getDescription(false).substring(4))
                .build();
        
        return new ResponseEntity<>(errorResponse, status);
    }
}