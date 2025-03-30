package com.vision_rent.automovil_unite.application.dto;


import com.vision_rent.automovil_unite.domain.valueobject.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO para transferir datos de usuario entre capas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
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
}