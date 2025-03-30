package com.vision_rent.automovil_unite.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entidad JPA que representa un método de pago en la base de datos.
 */
@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodJpaEntity extends BaseJpaEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;
    
    @Column(nullable = false)
    private String type;
    
    @Column(nullable = false)
    private String provider;
    
    @Column(nullable = false)
    private String alias;
    
    @Column(name = "encrypted_card_number")
    private String encryptedCardNumber;
    
    @Column(name = "encrypted_expiry_date")
    private String encryptedExpiryDate;
    
    @Column(name = "tokenized_data")
    private String tokenizedData;
    
    @Column(name = "paypal_email")
    private String paypalEmail;
    
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;
}