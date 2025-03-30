package com.vision_rent.automovil_unite.presentation.controller;


import com.vision_rent.automovil_unite.application.dto.AuthenticationRequest;
import com.vision_rent.automovil_unite.application.dto.AuthenticationResponse;
import com.vision_rent.automovil_unite.application.dto.RegisterRequest;
import com.vision_rent.automovil_unite.application.dto.ResetPasswordRequest;
import com.vision_rent.automovil_unite.application.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para operaciones de autenticación.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    
    /**
     * Registra un nuevo usuario.
     *
     * @param request Datos de registro
     * @return Respuesta con token JWT
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    
    /**
     * Autentica un usuario.
     *
     * @param request Credenciales de autenticación
     * @return Respuesta con token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
    
    /**
     * Solicita el restablecimiento de contraseña.
     *
     * @param request Email para restablecer contraseña
     * @return Respuesta vacía
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        authService.requestPasswordReset(request);
        return ResponseEntity.ok().build();
    }
}