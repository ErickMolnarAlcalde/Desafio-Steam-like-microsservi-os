package com.desafio.user_and_wallet_service.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {

    private String message;

    private String detail;

    private LocalDateTime time;
}
