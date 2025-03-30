package com.vision_rent.automovil_unite.presentation.controller;


import com.vision_rent.automovil_unite.application.dto.UserDto;
import com.vision_rent.automovil_unite.application.service.UserService;
import com.vision_rent.automovil_unite.infrastructure.security.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Controlador para operaciones relacionadas con usuarios.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    
    /**
     * Obtiene el perfil del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return DTO del usuario
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.getUserById(userDetails.getUserId()));
    }
    
    /**
     * Actualiza el perfil del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param userDto Datos actualizados del usuario
     * @return DTO del usuario actualizado
     */
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(userDetails.getUserId(), userDto));
    }
    
    /**
     * Sube una foto de perfil.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param file Archivo de imagen
     * @return DTO del usuario actualizado
     * @throws IOException Si ocurre un error al subir la imagen
     */
    @PostMapping(value = "/me/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> uploadProfilePhoto(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.uploadProfilePhoto(userDetails.getUserId(), file));
    }
    
    /**
     * Sube una foto del DNI.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param file Archivo de imagen
     * @return DTO del usuario actualizado
     * @throws IOException Si ocurre un error al subir la imagen
     */
    @PostMapping(value = "/me/id-card", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> uploadIdCard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.uploadIdCardPhoto(userDetails.getUserId(), file));
    }
    
    /**
     * Sube un documento de antecedentes penales.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param file Archivo de documento
     * @return DTO del usuario actualizado
     * @throws IOException Si ocurre un error al subir el documento
     */
    @PostMapping(value = "/me/criminal-record", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> uploadCriminalRecord(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.uploadCriminalRecord(userDetails.getUserId(), file));
    }
    
    /**
     * Sube una foto de la licencia de conducir.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param file Archivo de imagen
     * @return DTO del usuario actualizado
     * @throws IOException Si ocurre un error al subir la imagen
     */
    @PostMapping(value = "/me/driver-license", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> uploadDriverLicense(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.uploadDriverLicense(userDetails.getUserId(), file));
    }
    
    /**
     * Obtiene un usuario por su ID (solo admin).
     *
     * @param id ID del usuario
     * @return DTO del usuario
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    /**
     * Obtiene los arrendatarios mejor calificados.
     *
     * @param limit Límite de resultados
     * @return Lista de DTOs de usuario
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<UserDto>> getTopRatedRenters(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(userService.getTopRatedRenters(limit));
    }
    
    /**
     * Verifica si el usuario tiene todos los documentos requeridos.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return true si el usuario tiene todos los documentos requeridos
     */
    @GetMapping("/me/has-required-documents")
    public ResponseEntity<Boolean> hasRequiredDocuments(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.hasRequiredDocuments(userDetails.getUserId()));
    }

    @PutMapping("/users/{id}/verify-email")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> verifyUserEmail(@PathVariable Long id) {
        userService.verifyUserEmail(id);
        return ResponseEntity.ok().build();
    }
}