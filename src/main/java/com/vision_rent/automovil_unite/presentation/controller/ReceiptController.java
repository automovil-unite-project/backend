package com.vision_rent.automovil_unite.presentation.controller;

import com.vision_rent.automovil_unite.application.dto.ReceiptDto;
import com.vision_rent.automovil_unite.application.service.ReceiptService;
import com.vision_rent.automovil_unite.infrastructure.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para operaciones relacionadas con boletas/recibos.
 */
@RestController
@RequestMapping("/api/v1/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;
    
    /**
     * Genera una boleta para un pago.
     *
     * @param paymentId ID del pago
     * @param userDetails Detalles del usuario autenticado
     * @return DTO de la boleta generada
     */
    @PostMapping("/generate")
    public ResponseEntity<ReceiptDto> generateReceipt(
            @RequestParam Long paymentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(receiptService.generateReceipt(paymentId));
    }
    
    /**
     * Obtiene una boleta por su ID.
     *
     * @param id ID de la boleta
     * @param userDetails Detalles del usuario autenticado
     * @return DTO de la boleta
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReceiptDto> getReceiptById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(receiptService.getReceiptById(id, userDetails.getUserId()));
    }
    
    /**
     * Obtiene una boleta por su número.
     *
     * @param receiptNumber Número de la boleta
     * @param userDetails Detalles del usuario autenticado
     * @return DTO de la boleta
     */
    @GetMapping("/number/{receiptNumber}")
    public ResponseEntity<ReceiptDto> getReceiptByNumber(
            @PathVariable String receiptNumber,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(receiptService.getReceiptByNumber(receiptNumber, userDetails.getUserId()));
    }
    
    /**
     * Obtiene todas las boletas del usuario autenticado como arrendatario.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de boleta
     */
    @GetMapping("/as-renter")
    public ResponseEntity<List<ReceiptDto>> getReceiptsAsRenter(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(receiptService.getReceiptsByRenterId(userDetails.getUserId()));
    }
    
    /**
     * Obtiene todas las boletas del usuario autenticado como propietario.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de boleta
     */
    @GetMapping("/as-owner")
    public ResponseEntity<List<ReceiptDto>> getReceiptsAsOwner(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(receiptService.getReceiptsByOwnerId(userDetails.getUserId()));
    }
    
    /**
     * Obtiene todas las boletas de un alquiler.
     *
     * @param rentalId ID del alquiler
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de boleta
     */
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<List<ReceiptDto>> getReceiptsByRentalId(
            @PathVariable Long rentalId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(receiptService.getReceiptsByRentalId(rentalId, userDetails.getUserId()));
    }
}