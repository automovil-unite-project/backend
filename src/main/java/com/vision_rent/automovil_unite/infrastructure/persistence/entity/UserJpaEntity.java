package com.vision_rent.automovil_unite.infrastructure.persistence.entity;

import com.vision_rent.automovil_unite.domain.valueobject.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA que representa un usuario en la base de datos.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserJpaEntity extends BaseJpaEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String phone;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)

    private Set<Role> roles = new HashSet<>();

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

    @Column(name = "id_card_photo_url")
    private String idCardPhotoUrl;

    @Column(name = "criminal_record_url")
    private String criminalRecordUrl;

    @Column(name = "driver_license_url")
    private String driverLicenseUrl;

    @Column(nullable = false)
    private boolean banned;

    @Column(name = "average_rating")
    private Float averageRating;

    @Column(name = "report_count")
    private int reportCount;

}