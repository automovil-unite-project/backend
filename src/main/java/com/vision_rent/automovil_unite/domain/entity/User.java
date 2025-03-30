package com.vision_rent.automovil_unite.domain.entity;

import com.vision_rent.automovil_unite.domain.valueobject.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad de dominio que representa un usuario del sistema.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean enabled;
    private boolean emailVerified;
    private Set<Role> roles;
    private String profilePhotoUrl;
    private String idCardPhotoUrl;
    private String criminalRecordUrl;
    private String driverLicenseUrl;
    private boolean banned;
    private Float averageRating;
    private int reportCount;
    
    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = true;
        this.emailVerified = false;
        this.roles = new HashSet<>();
        this.banned = false;
        this.averageRating = 0.0f;
        this.reportCount = 0;
    }
    
    public boolean hasRole(Role role) {
        return roles.contains(role);
    }
    
    public boolean isRenter() {
        return hasRole(Role.RENTER);
    }
    
    public boolean isOwner() {
        return hasRole(Role.OWNER);
    }
    
    public boolean isAdmin() {
        return hasRole(Role.ADMIN);
    }
    
    public boolean isEligibleForDiscount() {
        return averageRating != null && averageRating >= 4.7f && reportCount == 0;
    }
}