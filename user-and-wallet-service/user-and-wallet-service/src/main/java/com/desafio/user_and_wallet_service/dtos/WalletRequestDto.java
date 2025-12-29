package com.desafio.user_and_wallet_service.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class WalletRequestDto {

    private BigDecimal value;
    private String email;

}
