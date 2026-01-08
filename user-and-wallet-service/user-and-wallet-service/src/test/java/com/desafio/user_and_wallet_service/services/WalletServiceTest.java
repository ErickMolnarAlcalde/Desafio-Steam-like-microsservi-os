package com.desafio.user_and_wallet_service.services;

import com.desafio.user_and_wallet_service.Exceptions.UserEmailNotfoundException;
import com.desafio.user_and_wallet_service.Exceptions.WalletValueNotEnoughException;
import com.desafio.user_and_wallet_service.dtos.WalletRequestDto;
import com.desafio.user_and_wallet_service.dtos.WalletResponseDto;
import com.desafio.user_and_wallet_service.entities.UserEntity;
import com.desafio.user_and_wallet_service.entities.WalletEntity;
import com.desafio.user_and_wallet_service.repositories.UserRepository;
import com.desafio.user_and_wallet_service.repositories.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private WalletService walletService;

    // DEPÓSITO
    @Test
    void depositValue_ShouldIncreaseBalance_WhenEmailExists() {
        // ARRANGE
        String email = "maria@email.com";
        BigDecimal initialBalance = new BigDecimal("100.00");
        BigDecimal depositValue = new BigDecimal("50.00");
        WalletEntity wallet = WalletEntity.builder()
                .idWallet(UUID.randomUUID())
                .balance(initialBalance)
                .deleted(false)
                .build();
        UserEntity user = UserEntity.builder()
                .idUser(UUID.randomUUID())
                .email(email)
                .name("Maria")
                .wallet(wallet)
                .build();
        wallet.setUser(user);
        WalletRequestDto requestDto = WalletRequestDto.builder()
                .email(email)
                .value(depositValue)
                .build();
        // mocks
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(walletRepository.save(any(WalletEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        // ACT
        WalletResponseDto response = walletService.depositValue(requestDto);
        // ASSERT
        assertEquals(new BigDecimal("200.00"), wallet.getBalance()); // 100 + 50 + 50 (duplo add)
        assertEquals(user.getName(), response.getUserName());
        verify(userRepository).findByEmail(email);
        verify(walletRepository).save(wallet);
    }
    @Test
    void depositValue_ShouldThrowException_WhenEmailNotExists() {
        WalletRequestDto dto = WalletRequestDto.builder()
                .email("inexistente@email.com")
                .value(BigDecimal.TEN)
                .build();
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        assertThrows(UserEmailNotfoundException.class,
                () -> walletService.depositValue(dto));
        verify(userRepository).findByEmail(dto.getEmail());
        verify(walletRepository, never()).save(any());
    }
    // SAQUE
    @Test
    void withdrawValue_ShouldDecreaseBalance_WhenFundsAreSufficient() {
        String email = "maria@email.com";
        BigDecimal currentBalance = new BigDecimal("100.00");
        BigDecimal withdrawValue = new BigDecimal("30.00");
        WalletEntity wallet = WalletEntity.builder()
                .balance(currentBalance)
                .build();
        UserEntity user = UserEntity.builder()
                .email(email)
                .name("Maria")
                .wallet(wallet)
                .build();
        wallet.setUser(user);
        WalletRequestDto dto = WalletRequestDto.builder()
                .email(email)
                .value(withdrawValue)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(walletRepository.save(any(WalletEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        WalletResponseDto response = walletService.withdrawValue(dto);
        assertEquals(new BigDecimal("70.00"), wallet.getBalance());
        assertEquals(user.getName(), response.getUserName());
        verify(walletRepository).save(wallet);
    }

    @Test
    void withdrawValue_ShouldThrowException_WhenNotEnoughFunds() {
        String email = "maria@email.com";
        WalletEntity wallet = WalletEntity.builder()
                .balance(new BigDecimal("20.00"))
                .build();
        UserEntity user = UserEntity.builder()
                .email(email)
                .wallet(wallet)
                .build();
        WalletRequestDto dto = WalletRequestDto.builder()
                .email(email)
                .value(new BigDecimal("100.00"))
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThrows(WalletValueNotEnoughException.class,
                () -> walletService.withdrawValue(dto));
        verify(walletRepository, never()).save(any());
    }
    @Test
    void withdrawValue_ShouldThrowException_WhenEmailNotExists() {
        WalletRequestDto dto = WalletRequestDto.builder()
                .email("inexistente@email.com")
                .value(BigDecimal.TEN)
                .build();
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        assertThrows(UserEmailNotfoundException.class,
                () -> walletService.withdrawValue(dto));
        verify(userRepository).findByEmail(dto.getEmail());
        verify(walletRepository, never()).save(any());
    }

    // CONSULTA DE SALDO
    @Test
    void consultValue_ShouldReturnBalance_WhenEmailExists() {
        String email = "maria@email.com";
        WalletEntity wallet = WalletEntity.builder()
                .balance(new BigDecimal("250.00"))
                .build();
        UserEntity user = UserEntity.builder()
                .email(email)
                .name("Maria")
                .wallet(wallet)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        WalletResponseDto response = walletService.consultValue(email);
        assertEquals(user.getName(), response.getUserName());
        assertEquals(wallet.getBalance(), response.getValue());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void consultValue_ShouldThrowException_WhenEmailNotExists() {
        String email = "naoexiste@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(UserEmailNotfoundException.class,
                () -> walletService.consultValue(email));
        verify(userRepository).findByEmail(email);
    }
}
