package com.desafio.user_and_wallet_service.mappers;

import com.desafio.user_and_wallet_service.dtos.LoginRequestDto;
import com.desafio.user_and_wallet_service.dtos.UserRequestDto;
import com.desafio.user_and_wallet_service.dtos.UserResponseDto;
import com.desafio.user_and_wallet_service.entities.UserEntity;
import com.desafio.user_and_wallet_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponseException;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final UserRepository userRepository;


    public UserEntity toEntity(UserRequestDto requestDto){
        return UserEntity.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public UserEntity toAlter(LoginRequestDto requestDto){
        var userEntity = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(()->
                new RuntimeException("usuário não encontrado"));

        return UserEntity.builder()
                .name(userEntity.getName())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .build();
    }

    public UserResponseDto toResponse(UserEntity entity){
        return UserResponseDto.builder()
                .id(entity.getIdUser())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .build();
    }



}
