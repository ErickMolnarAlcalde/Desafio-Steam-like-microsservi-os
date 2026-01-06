package com.desafio.user_and_wallet_service.Consumer;

import com.desafio.user_and_wallet_service.dtos.EmailDto;
import com.desafio.user_and_wallet_service.entities.EmailModel;
import com.desafio.user_and_wallet_service.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenerEmail(@Payload EmailDto emailDTO) {
        EmailModel emailModel = new EmailModel();
        BeanUtils.copyProperties(emailDTO, emailModel);
        emailService.sendEmail(emailModel);
    }




}
