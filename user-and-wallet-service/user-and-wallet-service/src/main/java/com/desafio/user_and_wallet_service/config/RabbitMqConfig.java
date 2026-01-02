package com.desafio.user_and_wallet_service.config;



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

    /**
     * Conversor de mensagens genérico em texto (String <-> byte[])
     * Não depende de nenhuma lib externa como Jackson ou Gson.
     */
    @Bean
    public MessageConverter simpleJsonLikeMessageConverter() {
        return new MessageConverter() {

            @Override
            public Message toMessage(Object object, MessageProperties messageProperties) {
                try {
                    String payload;

                    // Se o objeto for String, vai direto.
                    if (object instanceof String) {
                        payload = (String) object;
                    } else {
                        // Serializa de forma simples com toString()
                        // (Aqui você poderia implementar seu próprio esquema, ex: JSON-B, etc)
                        payload = object.toString();
                    }

                    messageProperties.setContentType("text/plain");
                    return new Message(payload.getBytes(), messageProperties);
                } catch (Exception e) {
                    throw new org.springframework.amqp.AmqpException("Erro ao converter mensagem para bytes", e);
                }
            }

            @Override
            public Object fromMessage(Message message) {
                try {
                    // Retorna o corpo como string pura.
                    return new String(message.getBody());
                } catch (Exception e) {
                    throw new org.springframework.amqp.AmqpException("Erro ao converter bytes para mensagem", e);
                }
            }
        };
    }


}
