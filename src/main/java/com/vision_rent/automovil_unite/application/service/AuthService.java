package com.vision_rent.automovil_unite.application.service;


import com.vision_rent.automovil_unite.application.dto.*;
import com.vision_rent.automovil_unite.application.exception.EmailAlreadyExistsException;
import com.vision_rent.automovil_unite.application.exception.InvalidCredentialsException;
import com.vision_rent.automovil_unite.application.mapper.UserDtoMapper;
import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.repository.UserRepository;
import com.vision_rent.automovil_unite.domain.service.UserDomainService;
import com.vision_rent.automovil_unite.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * Servicio para la autenticación y gestión de usuarios.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDtoMapper userDtoMapper = UserDtoMapper.INSTANCE;
    
    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param request Datos del usuario a registrar
     * @return Respuesta de autenticación con token JWT
     */
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("El email ya está registrado");
        }
        
        // Crear el usuario
        User user = userDtoMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(new HashSet<>());
        
        // Agregar el rol especificado
        userDomainService.addRole(user, request.getRole());
        
        // Guardar el usuario
        User savedUser = userRepository.save(user);
        
        // Generar tokens
        UserDto userDto = userDtoMapper.toDto(savedUser);
        String jwtToken = jwtService.generateToken(mapUserToUserDetails(savedUser));
        String refreshToken = jwtService.generateRefreshToken(mapUserToUserDetails(savedUser));
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .user(userDto)
                .build();
    }
    
    /**
     * Autentica a un usuario en el sistema.
     *
     * @param request Credenciales de autenticación
     * @return Respuesta de autenticación con token JWT
     */
    @Transactional(readOnly = true)
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            
            // Obtener el usuario autenticado
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));
            
            // Generar tokens
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);
            
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .refreshToken(refreshToken)
                    .user(userDtoMapper.toDto(user))
                    .build();
        } catch (Exception e) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }
    }
    
    /**
     * Envía un correo para restablecer la contraseña de un usuario.
     *
     * @param request Solicitud de restablecimiento de contraseña
     * @return true si se envió el correo correctamente
     */
    @Transactional(readOnly = true)
    public boolean requestPasswordReset(ResetPasswordRequest request) {
        // Verificar si el usuario existe
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);
        
        if (user == null) {
            // No revelamos si el email existe o no por seguridad
            return true;
        }
        
        // TODO: Implementar envío de correo con token de restablecimiento
        
        return true;
    }
    
    // Helper method para mapear un User a UserDetails
    private UserDetails mapUserToUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> "ROLE_" + role.name())
                        .toArray(String[]::new))
                .build();
    }
}