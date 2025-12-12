package com.desafio.user_and_wallet_service.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserResponseDto {

    private UUID id;

    private String name;

    private LocalDateTime createdAt;
}
