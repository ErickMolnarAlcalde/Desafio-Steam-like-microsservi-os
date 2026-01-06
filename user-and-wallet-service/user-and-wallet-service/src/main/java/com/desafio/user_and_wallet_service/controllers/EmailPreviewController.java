package com.desafio.user_and_wallet_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/preview")
public class EmailPreviewController {

    // Endpoint que exibe o HTML do e-mail para visualização
    @GetMapping("/email")
    public ResponseEntity<String> previewEmail() {

        // --- aqui vai o mesmo conteúdo do seu e-mail, só que inline ---
        String html = """
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
            Nos jogos, assim como na vida, cada escolha molda o caminho. Aproveite a jornada, não apenas o destino.
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
""".formatted("Maria", LocalDateTime.now().getYear());

        // monta a resposta HTTP com tipo de conteúdo HTML para renderizar no navegador
        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(html);
    }

    @GetMapping("/deposit")
    public ResponseEntity<String> previewDepositEmail() {

        // nome fictício e valores simulados só para ver o layout
        String name = "Maria Gamer";
        BigDecimal depositValue = new BigDecimal("150.00");
        BigDecimal newBalance = new BigDecimal("1250.75");

        String actionTitle = "Depósito realizado com sucesso!";
        String phrase = "Suas conquistas ganham poder — mais recursos para sua próxima aventura!";

        String html = """
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
            <p style="color:#66fcf1; font-size:16px; margin:0;">Valor do depósito: <strong>R$ %.2f</strong></p>
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
            Continue evoluindo — as maiores vitórias surgem das pequenas decisões.
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
""".formatted(name, actionTitle, phrase, depositValue, newBalance, LocalDateTime.now().getYear());

        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(html);
    }

}