package com.desafio.user_and_wallet_service.services;


import com.desafio.user_and_wallet_service.entities.EmailModel;
import com.desafio.user_and_wallet_service.enums.StatusEmail;
import com.desafio.user_and_wallet_service.repositories.EmailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

            var mimeMessage = javaMailSender.createMimeMessage();
            var helper = new org.springframework.mail.javamail.MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailModel.getEmailTo());
            helper.setSubject(emailModel.getEmailSubject());
            helper.setFrom(emailFrom);

            // corpo HTML com tema mais "Steam"
            String htmlContent = """
<!DOCTYPE html>
<html lang="pt-BR">
  <body style="margin:0; padding:0; font-family:'Segoe UI',Roboto,sans-serif; background-color:#0b0c10; color:#c5c6c7;">
    <table align="center" width="600" cellpadding="0" cellspacing="0" style="background-color:#1f2833; border-radius:8px;">
      <tr>
        <td align="center" style="background:linear-gradient(90deg,#0b0c10,#1f2833,#0b0c10); padding:40px 20px;">
          <img src="https://cdn-icons-png.flaticon.com/512/1076/1076954.png" width="80" alt="Game Icon" />
          <h1 style="color:#66fcf1; margin:20px 0 0; font-size:28px;">Bem-vindo(a), %s!</h1>
          <h2 style="font-weight:normal; color:#45a29e; margin-top:8px;">Sua conta gamer foi criada com sucesso!</h2>
        </td>
      </tr>

      <tr>
        <td style="padding:30px 40px; text-align:center;">
          <p style="font-size:16px; color:#c5c6c7; line-height:1.5;">
            O universo dos jogos ficou mais divertido com você aqui! <br>
            Explore promoções, conquiste conquistas, troque cards e conecte-se com jogadores do mundo todo.
          </p>

          <div style="margin-top:25px;">
            <a href="#" 
               style="background-color:#45a29e; color:#0b0c10; text-decoration:none; 
                      padding:12px 24px; border-radius:4px; font-weight:bold; display:inline-block;">
              ACESSAR MINHA CONTA
            </a>
          </div>

          <p style="font-size:14px; color:#888; margin-top:25px;">
            Prepare seu headset, ajuste o brilho da tela e boa diversão!
          </p>
        </td>
      </tr>

      <tr>
        <td align="center" style="background-color:#0b0c10; padding:20px; border-top:1px solid #45a29e;">
          <p style="color:#45a29e; font-size:13px; margin:0;">
            Equipe AgiGames © %d · Todos os direitos reservados
          </p>
        </td>
      </tr>
    </table>
  </body>
</html>
""".formatted(emailModel.getName(), LocalDateTime.now().getYear());

            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            emailModel.setStatusEmail(StatusEmail.SENT);

        } catch (MailException e) {
            e.printStackTrace(); // para ver o motivo caso falhe
            emailModel.setStatusEmail(StatusEmail.ERROR);
        } finally {
            return emailRepository.save(emailModel);
        }
    }

    // --- envio de e-mail de depósito ---
    public EmailModel sendDepositEmail(String toAddress, String name, BigDecimal depositValue, BigDecimal newBalance) {
        return sendWalletEmail(toAddress, name, depositValue, newBalance, true);
    }

    // --- envio de e-mail de saque ---
    public EmailModel sendWithdrawEmail(String toAddress, String name, BigDecimal withdrawValue, BigDecimal newBalance) {
        return sendWalletEmail(toAddress, name, withdrawValue, newBalance, false);
    }

    // método comum: gera HTML diferente para depósito/saque
    private EmailModel sendWalletEmail(String toAddress, String name, BigDecimal amount, BigDecimal newBalance, boolean isDeposit) {
        EmailModel emailModel = new EmailModel();
        emailModel.setEmailTo(toAddress);
        emailModel.setEmailFrom(emailFrom);
        emailModel.setTimeStamp(LocalDateTime.now());
        emailModel.setName(name);
        emailModel.setEmailSubject(isDeposit ? "Depósito confirmado!" : "Saque realizado!");
        emailModel.setEmailText(isDeposit ?
                String.format("Depósito de R$ %.2f concluído. Novo saldo: R$ %.2f", amount, newBalance) :
                String.format("Saque de R$ %.2f realizado. Novo saldo: R$ %.2f", amount, newBalance));

        try {
            var mimeMessage = javaMailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toAddress);
            helper.setSubject(emailModel.getEmailSubject());
            helper.setFrom(emailFrom);

            // tema gamer reutilizado, com textos dinâmicos
            String actionTitle = isDeposit ? "Depósito realizado com sucesso!" : "Saque efetuado com sucesso!";
            String phrase = isDeposit
                    ? "Suas conquistas ganham poder — mais recursos para sua próxima aventura!"
                    : "Você se preparou para o desafio e usou seus recursos com sabedoria.";

            String htmlContent = """
<!DOCTYPE html>
<html lang="pt-BR">
  <body style="margin:0; padding:0; font-family:'Segoe UI',Roboto,sans-serif; background-color:#0b0c10; color:#c5c6c7;">
    <table align="center" width="600" cellpadding="0" cellspacing="0" style="background-color:#1f2833; border-radius:8px;">
      <tr>
        <td align="center" style="background:linear-gradient(90deg,#0b0c10,#1f2833,#0b0c10); padding:40px 20px;">
          <img src="https://cdn-icons-png.flaticon.com/512/1076/1076954.png" width="80" alt="Game Icon" />
          <h1 style="color:#66fcf1; margin:20px 0 0; font-size:26px;">Olá, %s!</h1>
          <h2 style="font-weight:normal; color:#45a29e; margin-top:10px;">%s</h2>
        </td>
      </tr>

      <tr>
        <td style="padding:30px 40px; text-align:center;">
          <p style="font-size:17px; color:#c5c6c7; line-height:1.6; margin-bottom:20px;">%s</p>

          <div style="background-color:#0b0c10; border-radius:8px; padding:20px; display:inline-block;">
            <p style="color:#66fcf1; font-size:16px; margin:0;">Valor da operação: <strong>R$ %.2f</strong></p>
            <p style="color:#45a29e; font-size:16px; margin:8px 0 0;">Saldo atual: <strong>R$ %.2f</strong></p>
          </div>

          <div style="margin-top:25px;">
            <a href="#" 
               style="background-color:#45a29e; color:#0b0c10; text-decoration:none; 
                      padding:12px 24px; border-radius:4px; font-weight:bold; display:inline-block;">
              ACESSAR CARTEIRA
            </a>
          </div>

          <p style="font-size:14px; color:#888; margin-top:30px;">
            Continue evoluindo — as maiores vitórias surgem das pequenas decisões.
          </p>
        </td>
      </tr>

      <tr>
        <td align="center" style="background-color:#0b0c10; padding:20px; border-top:1px solid #45a29e;">
          <p style="color:#45a29e; font-size:13px; margin:0;">
            Equipe AgiGames © %d · Todos os direitos reservados
          </p>
        </td>
      </tr>
    </table>
  </body>
</html>
""".formatted(name, actionTitle, phrase, amount, newBalance, LocalDateTime.now().getYear());

            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            emailModel.setStatusEmail(StatusEmail.SENT);

        } catch (MailException e) {
            e.printStackTrace();
            emailModel.setStatusEmail(StatusEmail.ERROR);
        } finally {
            return emailRepository.save(emailModel);
        }
    }




}