package com.vision_rent.automovil_unite.domain.impl;


import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.service.UserDomainService;
import com.vision_rent.automovil_unite.domain.valueobject.Role;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de dominio para operaciones relacionadas con usuarios.
 */
@Service
public class UserDomainServiceImpl implements UserDomainService {

    @Override
    public boolean canRentVehicle(User user) {
        if (!user.isEnabled() || user.isBanned()) {
            return false;
        }
        
        if (!user.isEmailVerified()) {
            return false;
        }
        
        if (!user.isRenter()) {
            return false;
        }
        
        return hasRequiredDocuments(user);
    }

    @Override
    public boolean canPublishVehicle(User user) {
        if (!user.isEnabled() || user.isBanned()) {
            return false;
        }
        
        if (!user.isEmailVerified()) {
            return false;
        }
        
        if (!user.isOwner()) {
            return false;
        }
        
        return hasRequiredDocumentsForOwner(user);
    }

    @Override
    public User updateUserRating(User user, Float newRating) {
        if (newRating < 1.0f || newRating > 5.0f) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
        
        if (user.getAverageRating() == null) {
            user.setAverageRating(newRating);
        } else {
            // Promedio ponderado simple
            float currentRating = user.getAverageRating();
            user.setAverageRating((currentRating + newRating) / 2);
        }
        
        return user;
    }

    @Override
    public User addRole(User user, Role role) {
        user.getRoles().add(role);
        return user;
    }

    @Override
    public boolean hasRequiredDocuments(User user) {
        if (user.isRenter()) {
            return hasRequiredDocumentsForRenter(user);
        } else if (user.isOwner()) {
            return hasRequiredDocumentsForOwner(user);
        }
        
        return false;
    }
    
    private boolean hasRequiredDocumentsForRenter(User user) {
        return user.getIdCardPhotoUrl() != null && 
               user.getCriminalRecordUrl() != null && 
               user.getDriverLicenseUrl() != null &&
               user.getProfilePhotoUrl() != null;
    }
    
    private boolean hasRequiredDocumentsForOwner(User user) {
        return user.getIdCardPhotoUrl() != null && 
               user.getCriminalRecordUrl() != null && 
               user.getProfilePhotoUrl() != null;
    }

    @Override
    public User banUser(User user) {
        user.setBanned(true);
        return user;
    }

    @Override
    public User unbanUser(User user) {
        user.setBanned(false);
        return user;
    }
}