package com.vision_rent.automovil_unite.domain.service;


import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.valueobject.Role;
import org.springframework.stereotype.Service;


/**
 * Servicio de dominio para operaciones relacionadas con usuarios.
 */

@Service
public interface UserDomainService {
    
    /**
     * Verifica si un usuario puede alquilar un vehículo.
     * 
     * @param user Usuario a verificar
     * @return true si el usuario puede alquilar, false en caso contrario
     */
    boolean canRentVehicle(User user);
    
    /**
     * Verifica si un usuario puede publicar un vehículo.
     * 
     * @param user Usuario a verificar
     * @return true si el usuario puede publicar, false en caso contrario
     */
    boolean canPublishVehicle(User user);
    
    /**
     * Actualiza la calificación promedio de un usuario.
     * 
     * @param user Usuario a actualizar
     * @param newRating Nueva calificación
     * @return Usuario actualizado
     */
    User updateUserRating(User user, Float newRating);
    
    /**
     * Agrega un rol a un usuario.
     * 
     * @param user Usuario a actualizar
     * @param role Rol a agregar
     * @return Usuario actualizado
     */
    User addRole(User user, Role role);
    
    /**
     * Verifica si un usuario tiene todos los documentos requeridos.
     * 
     * @param user Usuario a verificar
     * @return true si el usuario tiene todos los documentos, false en caso contrario
     */
    boolean hasRequiredDocuments(User user);
    
    /**
     * Suspende temporalmente a un usuario.
     * 
     * @param user Usuario a suspender
     * @return Usuario actualizado
     */
    User banUser(User user);
    
    /**
     * Reactiva a un usuario suspendido.
     * 
     * @param user Usuario a reactivar
     * @return Usuario actualizado
     */
    User unbanUser(User user);
}