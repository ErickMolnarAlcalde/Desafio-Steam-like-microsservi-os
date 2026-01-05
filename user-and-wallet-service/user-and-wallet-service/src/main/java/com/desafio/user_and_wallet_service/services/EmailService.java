package com.desafio.user_and_wallet_service.services;


import com.desafio.user_and_wallet_service.entities.EmailModel;
import com.desafio.user_and_wallet_service.enums.StatusEmail;
import com.desafio.user_and_wallet_service.repositories.EmailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {


    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;

    public EmailService(JavaMailSender javaMailSender, EmailRepository emailRepository) {
        this.javaMailSender = javaMailSender;
        this.emailRepository = emailRepository;
    }


    @Value(value = "${spring.mail.username}")
    private String emailFrom;
    @Transactional
    public EmailModel sendEmail(EmailModel emailModel) {
        try {
            emailModel.setTimeStamp(LocalDateTime.now());
            emailModel.setEmailFrom(emailFrom);

            // Cria o e-mail no formato HTML
            var mimeMessage = javaMailSender.createMimeMessage();
            var helper = new org.springframework.mail.javamail.MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailModel.getEmailTo());
            helper.setSubject(emailModel.getEmailSubject());
            helper.setFrom(emailFrom);

            // Monta o corpo HTML do e-mail
            String htmlContent = """
<html>
  <body style="font-family: Arial, sans-serif; background-color: #f2f5f7; padding: 0; margin: 0;">
    <table align="center" width="600" cellpadding="0" cellspacing="0"
           style="background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
      <!-- Cabeçalho -->
      <tr>
        <td align="center" style="background-color: #003366; padding: 30px 20px;">
          <h1 style="color: #ffffff; margin: 0; font-size: 26px;">
            Seja bem-vindo(a), %s!
          </h1>
          <h2 style="color: #b3c7e6; margin-top: 8px; font-weight: normal;">ao Agibank 💙</h2>
        </td>
      </tr>

      <!-- Corpo -->
      <tr>
        <td style="padding: 30px 40px; text-align: center;">
          <p style="font-size: 16px; color: #333333;">
            Agradecemos por escolher o <strong>Agibank</strong>! 
            Estamos muito felizes em tê-lo(a) conosco e prontos para oferecer 
            <strong>o melhor serviço bancário digital do país</strong>.
          </p>

          <p style="font-size: 16px; color: #333333; margin-top: 20px;">
            Aqui, sua jornada financeira é mais simples, segura e feita para você. 
            Conte conosco para conquistar seus objetivos!
          </p>

          <!-- Imagens sutis -->
          <div style="margin-top: 20px;">
            <img src="https://static.agibank.com.br/web_public/site/images-site/homem-poltrona.png" 
                 alt="Homem Poltrona" width="150" style="margin: 10px;"/>
            <img src="https://static.agibank.com.br/web_public/site/images-site/imagem-secao-por-que-agibank.png" 
                 alt="Por que Agibank" width="150" style="margin: 10px;"/>
          </div>
        </td>
      </tr>

      <!-- Rodapé -->
      <tr>
        <td align="center" style="background-color: #003366; padding: 15px;">
          <p style="color: #ffffff; font-size: 14px; margin: 0;">Atenciosamente,<br>Equipe Agibank</p>
        </td>
      </tr>
    </table>
  </body>
</html>
""".formatted(emailModel.getName());

            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            emailModel.setStatusEmail(StatusEmail.SENT);

        } catch (MailException e) {
            emailModel.setStatusEmail(StatusEmail.ERROR);
        } finally {
            return emailRepository.save(emailModel);
        }
    }




}