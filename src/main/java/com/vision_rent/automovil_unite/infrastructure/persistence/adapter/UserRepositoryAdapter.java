package com.vision_rent.automovil_unite.infrastructure.persistence.adapter;


import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.repository.UserRepository;
import com.vision_rent.automovil_unite.domain.valueobject.Role;
import com.vision_rent.automovil_unite.infrastructure.persistence.mapper.UserMapper;
import com.vision_rent.automovil_unite.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador para el repositorio de usuarios que implementa la interfaz de dominio.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    
    @Override
    public User save(User user) {
        var userJpaEntity = userMapper.toJpaEntity(user);
        var savedEntity = userJpaRepository.save(userJpaEntity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findByRole(Role role) {
        return userJpaRepository.findByRole(role)
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(User user) {
        userJpaRepository.deleteById(user.getId());
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public void updateRating(Long userId, Float newRating) {
        userJpaRepository.updateRating(userId, newRating);
    }

    @Override
    public void incrementReportCount(Long userId) {
        userJpaRepository.incrementReportCount(userId);
    }

    @Override
    public List<User> findTopRatedRenters(int limit) {
        return userJpaRepository.findTopRatedRenters(Role.RENTER)
                .stream()
                .limit(limit)
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
}