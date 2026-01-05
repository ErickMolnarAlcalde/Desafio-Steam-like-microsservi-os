package com.desafio.user_and_wallet_service.config;



import com.desafio.user_and_wallet_service.dtos.EmailDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${broker.queue.email.name}")
    private String queueName;

    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    public MessageConverter simpleJsonLikeMessageConverter() {
        return new MessageConverter() {

            private final com.fasterxml.jackson.databind.ObjectMapper mapper =
                    new com.fasterxml.jackson.databind.ObjectMapper();

            @Override
            public Message toMessage(Object object, MessageProperties messageProperties) {
                try {
                    // Serializa o objeto (EmailDto ou outro) em JSON
                    byte[] bytes = mapper.writeValueAsBytes(object);
                    messageProperties.setContentType("application/json");
                    return new Message(bytes, messageProperties);
                } catch (Exception e) {
                    throw new org.springframework.amqp.AmqpException("Erro ao converter objeto para JSON", e);
                }
            }

            @Override
            public Object fromMessage(Message message) {
                try {
                    // Garante que o contentType é JSON
                    if ("application/json".equalsIgnoreCase(message.getMessageProperties().getContentType())) {
                        // Desserializa de volta para EmailDto
                        return mapper.readValue(message.getBody(), EmailDto.class);
                    } else {
                        // fallback pra texto puro, caso algo venha como plain text
                        return new String(message.getBody());
                    }
                } catch (Exception e) {
                    throw new org.springframework.amqp.AmqpException("Erro ao converter JSON para objeto", e);
                }
            }
        };
    }


}
