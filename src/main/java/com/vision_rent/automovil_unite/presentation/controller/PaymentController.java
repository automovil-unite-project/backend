package com.vision_rent.automovil_unite.presentation.controller;

import com.vision_rent.automovil_unite.application.dto.PaymentDto;
import com.vision_rent.automovil_unite.application.dto.ProcessPaymentRequest;
import com.vision_rent.automovil_unite.application.service.PaymentService;
import com.vision_rent.automovil_unite.infrastructure.security.impl.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para operaciones relacionadas con pagos.
 */
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    
    /**
     * Procesa un pago.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param request Datos del pago
     * @return DTO del pago procesado
     */
    @PostMapping("/process")
    public ResponseEntity<PaymentDto> processPayment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ProcessPaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(userDetails.getUserId(), request));
    }
    
    /**
     * Obtiene un pago por su ID.
     *
     * @param id ID del pago
     * @param userDetails Detalles del usuario autenticado
     * @return DTO del pago
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(paymentService.getPaymentById(id, userDetails.getUserId()));
    }
    
    /**
     * Obtiene todos los pagos realizados por el usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de pago
     */
    @GetMapping("/my-payments")
    public ResponseEntity<List<PaymentDto>> getMyPayments(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(paymentService.getPaymentsByUserId(userDetails.getUserId()));
    }
    
    /**
     * Obtiene todos los pagos de un alquiler.
     *
     * @param rentalId ID del alquiler
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de pago
     */
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByRentalId(
            @PathVariable Long rentalId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(paymentService.getPaymentsByRentalId(rentalId, userDetails.getUserId()));
    }
}