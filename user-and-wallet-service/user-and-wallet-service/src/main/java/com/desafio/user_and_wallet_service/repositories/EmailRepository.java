package com.desafio.user_and_wallet_service.repositories;

import com.desafio.user_and_wallet_service.entities.EmailModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailRepository extends JpaRepository<EmailModel, UUID> {
}
