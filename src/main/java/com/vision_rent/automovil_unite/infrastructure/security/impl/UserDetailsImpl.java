package com.vision_rent.automovil_unite.infrastructure.security.impl;


import com.vision_rent.automovil_unite.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementación de UserDetails para la autenticación de Spring Security.
 */
@Data
@Builder
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    private final boolean enabled;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Long userId;

    /**
     * Crea una instancia de UserDetailsImpl a partir de un User.
     *
     * @param user Entidad de usuario
     * @return Instancia de UserDetailsImpl
     */
    public static UserDetailsImpl build(User user) {
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());

        return UserDetailsImpl.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .enabled(user.isEnabled() && !user.isBanned())
                .accountNonExpired(true)
                .accountNonLocked(!user.isBanned())
                .credentialsNonExpired(true)
                .authorities(authorities)
                .userId(user.getId())
                .build();
    }
}