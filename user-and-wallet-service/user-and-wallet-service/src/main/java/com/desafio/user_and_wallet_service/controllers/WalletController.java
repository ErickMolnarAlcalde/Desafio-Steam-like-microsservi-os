package com.desafio.user_and_wallet_service.controllers;

import com.desafio.user_and_wallet_service.dtos.WalletRequestDto;
import com.desafio.user_and_wallet_service.dtos.WalletResponseDto;
import com.desafio.user_and_wallet_service.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private WalletService walletService;

    @PostMapping("/deposit")
    public ResponseEntity<WalletResponseDto> depositValue(WalletRequestDto requestDto){
        return ResponseEntity.ok().body(walletService.depositValue(requestDto));

    }

    @PostMapping("/withdraw")
    public ResponseEntity<WalletResponseDto> withdrawValue(WalletRequestDto requestDto){
        return ResponseEntity.ok().body(walletService.withdrawValue(requestDto));
    }

    @GetMapping("/consult/{email}")
    public ResponseEntity<WalletResponseDto> withdrawValue(@RequestParam String email){
        return ResponseEntity.ok().body(walletService.consultValue(email));
    }
}
