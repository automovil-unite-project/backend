package com.vision_rent.automovil_unite.presentation.controller;

import com.vision_rent.automovil_unite.application.dto.CreatePaymentMethodRequest;
import com.vision_rent.automovil_unite.application.dto.PaymentMethodDto;
import com.vision_rent.automovil_unite.application.service.PaymentMethodService;

import com.vision_rent.automovil_unite.infrastructure.security.impl.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para operaciones relacionadas con métodos de pago.
 */
@RestController
@RequestMapping("/api/v1/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;
    
    /**
     * Crea un nuevo método de pago.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param request Datos del método de pago
     * @return DTO del método de pago creado
     */
    @PostMapping
    public ResponseEntity<PaymentMethodDto> createPaymentMethod(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreatePaymentMethodRequest request) {
        return ResponseEntity.ok(paymentMethodService.createPaymentMethod(userDetails.getUserId(), request));
    }
    
    /**
     * Obtiene todos los métodos de pago del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de método de pago
     */
    @GetMapping
    public ResponseEntity<List<PaymentMethodDto>> getMyPaymentMethods(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(paymentMethodService.getPaymentMethodsByUserId(userDetails.getUserId()));
    }
    
    /**
     * Obtiene un método de pago por su ID.
     *
     * @param id ID del método de pago
     * @param userDetails Detalles del usuario autenticado
     * @return DTO del método de pago
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDto> getPaymentMethodById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(paymentMethodService.getPaymentMethodById(id, userDetails.getUserId()));
    }
    
    /**
     * Elimina un método de pago.
     *
     * @param id ID del método de pago
     * @param userDetails Detalles del usuario autenticado
     * @return Respuesta vacía
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        paymentMethodService.deletePaymentMethod(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Establece un método de pago como predeterminado.
     *
     * @param id ID del método de pago
     * @param userDetails Detalles del usuario autenticado
     * @return DTO del método de pago actualizado
     */
    @PutMapping("/{id}/default")
    public ResponseEntity<PaymentMethodDto> setAsDefault(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(paymentMethodService.setAsDefault(id, userDetails.getUserId()));
    }
    
    /**
     * Obtiene el método de pago predeterminado del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return DTO del método de pago predeterminado
     */
    @GetMapping("/default")
    public ResponseEntity<PaymentMethodDto> getDefaultPaymentMethod(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PaymentMethodDto paymentMethod = paymentMethodService.getDefaultPaymentMethod(userDetails.getUserId());
        if (paymentMethod == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(paymentMethod);
    }
}