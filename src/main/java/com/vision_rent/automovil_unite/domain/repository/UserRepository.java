package com.vision_rent.automovil_unite.domain.repository;



import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.valueobject.Role;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad User en el dominio.
 * Define las operaciones permitidas de persistencia para los usuarios.
 */

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findAll();
    void delete(User user);
    boolean existsByEmail(String email);
    void updateRating(Long userId, Float newRating);
    void incrementReportCount(Long userId);
    List<User> findTopRatedRenters(int limit);
}