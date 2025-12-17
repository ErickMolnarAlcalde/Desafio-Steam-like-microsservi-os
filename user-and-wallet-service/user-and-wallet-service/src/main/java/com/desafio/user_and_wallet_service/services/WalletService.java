package com.desafio.user_and_wallet_service.services;

import com.desafio.user_and_wallet_service.Exceptions.UserEmailAlreadyExistsException;
import com.desafio.user_and_wallet_service.Exceptions.UserEmailNotfoundException;
import com.desafio.user_and_wallet_service.dtos.WalletResponseDto;
import com.desafio.user_and_wallet_service.repositories.UserRepository;
import com.desafio.user_and_wallet_service.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletResponseDto depositValue(BigDecimal value, String email){
        var entity = userRepository.findByEmail(email).orElseThrow(()->
                new UserEmailNotfoundException("email não encontrado!"));
        entity.getWallet().setBalance(entity.getWallet().getBalance().add(value));
        var wallet = entity.getWallet();
        wallet.setBalance(wallet.getBalance().add(value));
        walletRepository.save(wallet);

        return WalletResponseDto.builder()
                .userName(entity.getName())
                .value(entity.getWallet().getBalance())
                .build();
    }

    public WalletResponseDto withdrawValue(BigDecimal value, String email){
        var entity = userRepository.findByEmail(email).orElseThrow(()->
                new UserEmailNotfoundException("email não encontrado!"));

        var wallet = entity.getWallet();
        wallet.setBalance(wallet.getBalance().subtract(value));
        walletRepository.save(wallet);

        return WalletResponseDto.builder()
                .userName(entity.getName())
                .value(entity.getWallet().getBalance())
                .build();
    }

    public WalletResponseDto consultValue(String email){
        var entity = userRepository.findByEmail(email).orElseThrow(()->
                new UserEmailAlreadyExistsException("email não encontrado!"));

        return WalletResponseDto.builder()
                .userName(entity.getName())
                .value(entity.getWallet().getBalance())
                .build();
    }






}
