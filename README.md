🎮 Steam‑Like Challenge — Plataforma de Jogos Digitais
“Uma Steam de bolso feita com Spring e muito café ☕”

🕹️ O que é isso?
Este projeto é um desafio técnico para criar uma plataforma de distribuição digital de jogos, inspirada na maneira como a Steam funciona.

Aqui o jogador pode:

Criar uma conta e uma carteira virtual 💰
Comprar jogos, gerenciar sua biblioteca 🎒
Fazer downloads com progresso assíncrono 🚀
Receber notificações quando algo legal acontece 🔔
Tudo isso dividido em vários microsserviços que se conversam por REST, RabbitMQ e Kafka — como uma verdadeira conversa entre jogadores no lobby!

⚙️ Tecnologias que movem essa bagunça organizada
Java 21 + Spring Boot 3
RabbitMQ e Kafka
PostgreSQL / MySQL + MongoDB
Docker e Docker Compose
JUnit 5 + Mockito para os testes
@Async para simular aquela barrinha de download que nunca termina 😅
(Swagger pode vir como bônus, mas o desafio não exige — ainda assim, quem não gosta de ver endpoints bonitinhos?)

🧩 Microsserviços no mapa
Serviço	O que ele faz	Banco
🧍 user-and-wallet-service	Cadastro, login e carteira virtual	Relacional
🛒 game-store-service	Catálogo, compras e eventos de compra	Relacional
📚 library-and-download-service	Biblioteca e downloads (com logs)	SQL + MongoDB
🔔 notification-and-analytics-service	Notificações e relatórios	MongoDB
Cada um vive em seu container Docker e todos se juntam via docker-compose para formar o universo da plataforma.

🚀 Como rodar tudo
Clone o repositório (ou todos os serviços se forem separados).
Tenha Docker e Docker Compose instalados.
Rode:
bash
Copiar código
docker compose up --build
Espere a mágica acontecer ✨ (RabbitMQ, Kafka e bancos sobem junto).
Depois é só brincar com as APIs (ou usar Swagger se você adicionar).

🧪 Testes
Todos os serviços têm testes unitários com JUnit 5 e Mockito.
Cobrem criação de usuários, validação de saldo, compras, e até simulação de downloads.
Cobertura mínima recomendada: 70% (e umas risadas ao rodar).
🗂️ Organização no GitHub
Commits diários com mensagens semânticas:
feat: nova funcionalidade
fix: correção de bug
refactor: ajustes no código
test: novos testes
docs: atualização de documentação
A ideia é mostrar evolução constante — tipo XP no jogo 🎯

🧠 Objetivo Final
Mais do que chegar a um sistema funcional, o desafio é mostrar domínio de arquitetura moderna:

Microsserviços bem isolados.
Comunicação síncrona e assíncrona.
Processamento por eventos.
Testes automáticos e containers.
Em outras palavras: um ambiente que parece um jogo multiplayer, mas é puramente código.

🙌 Feito por quem ainda acredita que código pode ser divertido
“Aprender novas tecnologias é estilo de vida, não obrigação.”
