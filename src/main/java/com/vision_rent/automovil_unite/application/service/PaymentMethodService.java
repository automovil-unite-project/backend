package com.vision_rent.automovil_unite.application.service;

import com.vision_rent.automovil_unite.application.dto.CreatePaymentMethodRequest;
import com.vision_rent.automovil_unite.application.dto.PaymentMethodDto;
import com.vision_rent.automovil_unite.application.exception.InvalidOperationException;
import com.vision_rent.automovil_unite.application.exception.ResourceNotFoundException;
import com.vision_rent.automovil_unite.application.mapper.PaymentMethodDtoMapper;
import com.vision_rent.automovil_unite.domain.entity.PaymentMethod;
import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.repository.PaymentMethodRepository;
import com.vision_rent.automovil_unite.domain.repository.UserRepository;
import com.vision_rent.automovil_unite.infrastructure.security.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de métodos de pago.
 */
@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final PaymentMethodDtoMapper paymentMethodDtoMapper;
    
    /**
     * Crea un nuevo método de pago.
     *
     * @param userId ID del usuario
     * @param request Datos del método de pago
     * @return DTO del método de pago creado
     */
    @Transactional
    public PaymentMethodDto createPaymentMethod(Long userId, CreatePaymentMethodRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        
        // Verificar si ya existe un alias con el mismo nombre
        if (paymentMethodRepository.existsByUserIdAndAlias(userId, request.getAlias())) {
            throw new InvalidOperationException("Ya existe un método de pago con ese alias");
        }
        
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setUser(user);
        paymentMethod.setType(request.getType());
        paymentMethod.setProvider(request.getProvider());
        paymentMethod.setAlias(request.getAlias());
        
        // Configurar según el tipo de método de pago
        if ("CREDIT_CARD".equals(request.getType()) || "DEBIT_CARD".equals(request.getType())) {
            // Encriptar información sensible
            paymentMethod.setEncryptedCardNumber(encryptionService.encrypt(request.getCardNumber()));
            paymentMethod.setEncryptedExpiryDate(encryptionService.encrypt(request.getExpiryDate()));
            
            // En un sistema real, se integraría con un gateway de pago para tokenizar
            paymentMethod.setTokenizedData("token-" + System.currentTimeMillis());
        } else if ("PAYPAL".equals(request.getType())) {
            paymentMethod.setPaypalEmail(request.getPaypalEmail());
        }
        
        // Configurar como método por defecto si se solicita
        boolean setAsDefault = request.getSetAsDefault() != null ? request.getSetAsDefault() : false;
        paymentMethod.setIsDefault(setAsDefault);
        
        // Si se marca como predeterminado, desmarcar los otros métodos
        if (setAsDefault) {
            resetDefaultPaymentMethods(userId);
        }
        
        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        return paymentMethodDtoMapper.toDto(savedPaymentMethod);
    }
    
    /**
     * Obtiene todos los métodos de pago de un usuario.
     *
     * @param userId ID del usuario
     * @return Lista de DTOs de método de pago
     */
    @Transactional(readOnly = true)
    public List<PaymentMethodDto> getPaymentMethodsByUserId(Long userId) {
        return paymentMethodRepository.findByUserId(userId)
                .stream()
                .map(paymentMethodDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene un método de pago por su ID.
     *
     * @param id ID del método de pago
     * @param userId ID del usuario que solicita
     * @return DTO del método de pago
     */
    @Transactional(readOnly = true)
    public PaymentMethodDto getPaymentMethodById(Long id, Long userId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Método de pago", "id", id));
        
        // Verificar que el método de pago pertenece al usuario
        if (!paymentMethod.getUser().getId().equals(userId)) {
            throw new InvalidOperationException("No tiene permiso para acceder a este método de pago");
        }
        
        return paymentMethodDtoMapper.toDto(paymentMethod);
    }
    
    /**
     * Elimina un método de pago.
     *
     * @param id ID del método de pago
     * @param userId ID del usuario que solicita
     */
    @Transactional
    public void deletePaymentMethod(Long id, Long userId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Método de pago", "id", id));
        
        // Verificar que el método de pago pertenece al usuario
        if (!paymentMethod.getUser().getId().equals(userId)) {
            throw new InvalidOperationException("No tiene permiso para eliminar este método de pago");
        }
        
        // Si es el método por defecto, no permitir eliminar si es el único
        if (paymentMethod.getIsDefault() && paymentMethodRepository.findByUserId(userId).size() <= 1) {
            throw new InvalidOperationException("No puede eliminar su único método de pago");
        }
        
        paymentMethodRepository.delete(paymentMethod);
    }
    
    /**
     * Establece un método de pago como predeterminado.
     *
     * @param id ID del método de pago
     * @param userId ID del usuario que solicita
     * @return DTO del método de pago actualizado
     */
    @Transactional
    public PaymentMethodDto setAsDefault(Long id, Long userId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Método de pago", "id", id));
        
        // Verificar que el método de pago pertenece al usuario
        if (!paymentMethod.getUser().getId().equals(userId)) {
            throw new InvalidOperationException("No tiene permiso para modificar este método de pago");
        }
        
        // Desmarcar los otros métodos de pago como predeterminados
        resetDefaultPaymentMethods(userId);
        
        // Marcar este como predeterminado
        paymentMethod.setIsDefault(true);
        
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        return paymentMethodDtoMapper.toDto(updatedPaymentMethod);
    }
    
    /**
     * Encuentra el método de pago predeterminado de un usuario.
     *
     * @param userId ID del usuario
     * @return DTO del método de pago predeterminado, o null si no tiene
     */
    @Transactional(readOnly = true)
    public PaymentMethodDto getDefaultPaymentMethod(Long userId) {
        return paymentMethodRepository.findDefaultByUserId(userId)
                .map(paymentMethodDtoMapper::toDto)
                .orElse(null);
    }
    
    /**
     * Desmarca todos los métodos de pago de un usuario como predeterminados.
     * 
     * @param userId ID del usuario
     */
    private void resetDefaultPaymentMethods(Long userId) {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByUserId(userId);
        for (PaymentMethod pm : paymentMethods) {
            if (pm.getIsDefault()) {
                pm.setIsDefault(false);
                paymentMethodRepository.save(pm);
            }
        }
    }
}