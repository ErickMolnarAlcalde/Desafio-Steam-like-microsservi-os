package com.desafio.user_and_wallet_service.services;

import com.desafio.user_and_wallet_service.entities.EmailModel;
import com.desafio.user_and_wallet_service.enums.StatusEmail;
import com.desafio.user_and_wallet_service.repositories.EmailRepository;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private EmailRepository emailRepository;

    private EmailService emailService;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService(javaMailSender, emailRepository);

        Field emailFromField = EmailService.class.getDeclaredField("emailFrom");
        emailFromField.setAccessible(true);
        emailFromField.set(emailService, "noreply@agigames.com");
    }

    @Test
    void deveEnviarEmailComSucesso() {
        // Arrange
        EmailModel model = EmailModel.builder()
                .idEmail(UUID.randomUUID())
                .emailTo("user@example.com")
                .emailSubject("Bem-vindo")
                .emailText("Seu cadastro foi concluído!")
                .name("Usuário Teste")
                .build();

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailRepository.save(any(EmailModel.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        EmailModel result = emailService.sendEmail(model);

        // Assert
        verify(javaMailSender, times(1)).send(mimeMessage);
        verify(emailRepository, times(1)).save(any(EmailModel.class));
        assertEquals(StatusEmail.SENT, result.getStatusEmail());
        assertNotNull(result.getTimeStamp());
        assertEquals("noreply@agigames.com", result.getEmailFrom());
    }

    @Test
    void deveMarcarStatusComoError_QuandoMailException() {
        // Arrange
        EmailModel model = EmailModel.builder()
                .emailTo("user@example.com")
                .emailSubject("Erro Teste")
                .emailText("Conteúdo")
                .name("User")
                .build();

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailException("Falha no envio") {}).when(javaMailSender).send(any(MimeMessage.class));
        when(emailRepository.save(any(EmailModel.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        EmailModel result = emailService.sendEmail(model);

        // Assert
        verify(emailRepository).save(any(EmailModel.class));
        assertEquals(StatusEmail.ERROR, result.getStatusEmail());
    }

    @Test
    void deveEnviarEmailDeDeposito() {
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        when(emailRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        var result = emailService.sendDepositEmail("user@example.com", "Player", new BigDecimal("50.00"), new BigDecimal("150.00"));

        // Assert
        assertEquals(StatusEmail.SENT, result.getStatusEmail());
        assertEquals("Depósito confirmado!", result.getEmailSubject());
        verify(javaMailSender).send(any(MimeMessage.class));
        verify(emailRepository).save(any());
    }

    @Test
    void deveEnviarEmailDeSaque() {
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        when(emailRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        var result = emailService.sendWithdrawEmail("user@example.com", "Player", new BigDecimal("20.00"), new BigDecimal("130.00"));

        // Assert
        assertEquals(StatusEmail.SENT, result.getStatusEmail());
        assertEquals("Saque realizado!", result.getEmailSubject());
        verify(javaMailSender).send(any(MimeMessage.class));
        verify(emailRepository).save(any());
    }
}