package com.vision_rent.automovil_unite.application.mapper;

import com.vision_rent.automovil_unite.application.dto.PaymentMethodDto;
import com.vision_rent.automovil_unite.domain.entity.PaymentMethod;
import com.vision_rent.automovil_unite.infrastructure.security.EncryptionService;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre PaymentMethod (dominio) y PaymentMethodDto (aplicación).
 * Se implementa manualmente debido a la necesidad de desencriptar y enmascarar la información sensible.
 */
@Component
public class PaymentMethodDtoMapper {

    private final EncryptionService encryptionService;
    
    public PaymentMethodDtoMapper(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }
    
    /**
     * Convierte una entidad de dominio a un DTO.
     * 
     * @param paymentMethod Entidad de dominio
     * @return DTO
     */
    public PaymentMethodDto toDto(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        
        PaymentMethodDto dto = new PaymentMethodDto();
        dto.setId(paymentMethod.getId());
        dto.setUserId(paymentMethod.getUser().getId());
        dto.setType(paymentMethod.getType());
        dto.setProvider(paymentMethod.getProvider());
        dto.setAlias(paymentMethod.getAlias());
        dto.setIsDefault(paymentMethod.getIsDefault());
        
        // Procesar información específica según el tipo
        if ("CREDIT_CARD".equals(paymentMethod.getType()) || "DEBIT_CARD".equals(paymentMethod.getType())) {
            // Desencriptar y enmascarar el número de tarjeta
            String decryptedCardNumber = encryptionService.decrypt(paymentMethod.getEncryptedCardNumber());
            dto.setMaskedCardNumber(encryptionService.maskCardNumber(decryptedCardNumber));
            
            // Fecha de expiración (MM/YY)
            String expiryDate = encryptionService.decrypt(paymentMethod.getEncryptedExpiryDate());
            dto.setExpiryDateFormatted(expiryDate);
        } else if ("PAYPAL".equals(paymentMethod.getType())) {
            dto.setPaypalEmail(paymentMethod.getPaypalEmail());
        }
        
        return dto;
    }
    
    /**
     * Convierte un DTO a una entidad de dominio.
     * Este método no se utiliza directamente ya que la creación de PaymentMethod 
     * requiere procesamiento de datos sensibles que se realiza en el servicio.
     * 
     * @param dto DTO
     * @return Entidad de dominio (parcial, sin datos sensibles)
     */
    public PaymentMethod toEntity(PaymentMethodDto dto) {
        if (dto == null) {
            return null;
        }
        
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(dto.getId());
        paymentMethod.setType(dto.getType());
        paymentMethod.setProvider(dto.getProvider());
        paymentMethod.setAlias(dto.getAlias());
        paymentMethod.setIsDefault(dto.getIsDefault());
        
        if ("PAYPAL".equals(dto.getType())) {
            paymentMethod.setPaypalEmail(dto.getPaypalEmail());
        }
        
        return paymentMethod;
    }
}