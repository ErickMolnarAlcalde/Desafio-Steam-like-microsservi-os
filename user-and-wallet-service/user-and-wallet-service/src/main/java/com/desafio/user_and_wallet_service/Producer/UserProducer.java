package com.desafio.user_and_wallet_service.Producer;

import com.desafio.user_and_wallet_service.dtos.EmailDto;
import com.desafio.user_and_wallet_service.entities.UserEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {

    private final RabbitTemplate rabbitTemplate;
    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @Value(value = "${broker.queue.email.name}")
    private String routingKey;
    public void publishMessage(UserEntity userModel){
        EmailDto emailDTO = new EmailDto();
        emailDTO.setName(userModel.getName());
        emailDTO.setEmailTo(userModel.getEmail());
        emailDTO.setEmailSubject("Cadastro de conta!!");
        emailDTO.setEmailText(userModel.getName()+", seja bem vindo ao nosso banco!!!");
        rabbitTemplate.convertAndSend("",routingKey,emailDTO);
    }
}
