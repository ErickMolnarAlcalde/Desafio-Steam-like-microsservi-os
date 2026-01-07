package com.desafio.user_and_wallet_service.services;

import com.desafio.user_and_wallet_service.Exceptions.UserEmailAlreadyExistsException;
import com.desafio.user_and_wallet_service.Exceptions.UserEmailNotfoundException;
import com.desafio.user_and_wallet_service.Exceptions.WalletValueNotEnoughException;
import com.desafio.user_and_wallet_service.dtos.WalletRequestDto;
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
    private final EmailService emailService;

    public WalletResponseDto depositValue(WalletRequestDto requestDto){
        var entity = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(()->
                new UserEmailNotfoundException("email não encontrado!"));
        entity.getWallet().setBalance(entity.getWallet().getBalance().add(requestDto.getValue()));
        var wallet = entity.getWallet();
        wallet.setBalance(wallet.getBalance().add(requestDto.getValue()));
        walletRepository.save(wallet);
        emailService.sendDepositEmail(entity.getEmail(), entity.getName(), requestDto.getValue(), wallet.getBalance());


        return WalletResponseDto.builder()
                .userName(entity.getName())
                .value(entity.getWallet().getBalance())
                .build();
    }

    public WalletResponseDto withdrawValue(WalletRequestDto requestDto){
        var entity = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(()->
                new UserEmailNotfoundException("email não encontrado!"));
        var wallet = entity.getWallet();

        if(wallet.getBalance().compareTo(requestDto.getValue())<0){
            throw new WalletValueNotEnoughException("Retirada não autorizada");
        }

            wallet.setBalance(wallet.getBalance().subtract(requestDto.getValue()));
        walletRepository.save(wallet);
        emailService.sendWithdrawEmail(entity.getEmail(), entity.getName(), requestDto.getValue(), wallet.getBalance());


        return WalletResponseDto.builder()
                .userName(entity.getName())
                .value(entity.getWallet().getBalance())
                .build();
    }

    public WalletResponseDto consultValue(String email){
        var entity = userRepository.findByEmail(email).orElseThrow(()->
                new UserEmailNotfoundException("email não encontrado!"));

        return WalletResponseDto.builder()
                .userName(entity.getName())
                .value(entity.getWallet().getBalance())
                .build();
    }






}
