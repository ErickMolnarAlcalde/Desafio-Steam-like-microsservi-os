package com.desafio.user_and_wallet_service.repositories;


import com.desafio.user_and_wallet_service.entities.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

}
