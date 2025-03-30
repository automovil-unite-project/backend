package com.vision_rent.automovil_unite.application.service;


import com.vision_rent.automovil_unite.application.dto.UserDto;
import com.vision_rent.automovil_unite.application.exception.ResourceNotFoundException;
import com.vision_rent.automovil_unite.application.mapper.UserDtoMapper;
import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.repository.UserRepository;
import com.vision_rent.automovil_unite.domain.service.UserDomainService;
import com.vision_rent.automovil_unite.infrastructure.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de usuarios.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final FileStorageService fileStorageService;
    private final UserDtoMapper userDtoMapper;

    /**
     * Obtiene un usuario por su ID.
     *
     * @param userId ID del usuario
     * @return DTO del usuario
     */
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        
        return userDtoMapper.toDto(user);
    }

    @Transactional
    public void verifyUserEmail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        user.setEmailVerified(true);
        userRepository.save(user);
    }
    
    /**
     * Obtiene un usuario por su email.
     *
     * @param email Email del usuario
     * @return DTO del usuario
     */
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));
        
        return userDtoMapper.toDto(user);
    }
    
    /**
     * Actualiza los datos de un usuario.
     *
     * @param userId ID del usuario
     * @param userDto Datos actualizados del usuario
     * @return DTO del usuario actualizado
     */
    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        
        // Actualizar campos editables
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setPhone(userDto.getPhone());
        
        // No permitimos actualizar email, password o roles a través de este método
        
        User updatedUser = userRepository.save(existingUser);
        return userDtoMapper.toDto(updatedUser);
    }
    
    /**
     * Sube una foto de perfil para un usuario.
     *
     * @param userId ID del usuario
     * @param file Archivo de imagen
     * @return DTO del usuario actualizado
     */
    @Transactional
    public UserDto uploadProfilePhoto(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        
        String fileName = fileStorageService.storeFile(file, "users/" + userId + "/profile");
        user.setProfilePhotoUrl(fileName);
        
        User updatedUser = userRepository.save(user);
        return userDtoMapper.toDto(updatedUser);
    }
    
    /**
     * Sube una foto del DNI para un usuario.
     *
     * @param userId ID del usuario
     * @param file Archivo de imagen
     * @return DTO del usuario actualizado
     */
    @Transactional
    public UserDto uploadIdCardPhoto(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        
        String fileName = fileStorageService.storeFile(file, "users/" + userId + "/documents");
        user.setIdCardPhotoUrl(fileName);
        
        User updatedUser = userRepository.save(user);
        return userDtoMapper.toDto(updatedUser);
    }
    
    /**
     * Sube un documento de antecedentes penales para un usuario.
     *
     * @param userId ID del usuario
     * @param file Archivo de documento
     * @return DTO del usuario actualizado
     */
    @Transactional
    public UserDto uploadCriminalRecord(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        
        String fileName = fileStorageService.storeFile(file, "users/" + userId + "/documents");
        user.setCriminalRecordUrl(fileName);
        
        User updatedUser = userRepository.save(user);
        return userDtoMapper.toDto(updatedUser);
    }
    
    /**
     * Sube una foto de la licencia de conducir para un usuario.
     *
     * @param userId ID del usuario
     * @param file Archivo de imagen
     * @return DTO del usuario actualizado
     */
    @Transactional
    public UserDto uploadDriverLicense(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        
        String fileName = fileStorageService.storeFile(file, "users/" + userId + "/documents");
        user.setDriverLicenseUrl(fileName);
        
        User updatedUser = userRepository.save(user);
        return userDtoMapper.toDto(updatedUser);
    }
    
    /**
     * Obtiene una lista de los arrendatarios mejor calificados.
     *
     * @param limit Límite de resultados
     * @return Lista de DTOs de usuario
     */
    @Transactional(readOnly = true)
    public List<UserDto> getTopRatedRenters(int limit) {
        return userRepository.findTopRatedRenters(limit)
                .stream()
                .map(userDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Verifica si un usuario tiene todos los documentos requeridos.
     *
     * @param userId ID del usuario
     * @return true si el usuario tiene todos los documentos requeridos
     */
    @Transactional(readOnly = true)
    public boolean hasRequiredDocuments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        
        return userDomainService.hasRequiredDocuments(user);
    }
    
    /**
     * Suspende temporalmente a un usuario.
     *
     * @param userId ID del usuario
     * @return DTO del usuario actualizado
     */
    @Transactional
    public UserDto banUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        
        userDomainService.banUser(user);
        
        User updatedUser = userRepository.save(user);
        return userDtoMapper.toDto(updatedUser);
    }
    
    /**
     * Reactiva a un usuario suspendido.
     *
     * @param userId ID del usuario
     * @return DTO del usuario actualizado
     */
    @Transactional
    public UserDto unbanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        
        userDomainService.unbanUser(user);
        
        User updatedUser = userRepository.save(user);
        return userDtoMapper.toDto(updatedUser);
    }
}