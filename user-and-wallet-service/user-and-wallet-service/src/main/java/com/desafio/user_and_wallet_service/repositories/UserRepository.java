package com.desafio.user_and_wallet_service.repositories;

import com.desafio.user_and_wallet_service.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByName(String name);
    Optional<UserEntity> findByEmail(String email); // o find vai busca e traz o usuario completo
    boolean existsByEmail(String email); // o exist vai retornar se existe um usuario com este email(sim ou nao)
}
