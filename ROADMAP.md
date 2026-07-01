# Roadmap de Desenvolvimento

Este projeto foi estruturado para evoluir em sprints claras, mantendo a versão desktop existente e adicionando uma interface web em paralelo.

## Sprint 1 — Fundação e organização
- Definir a arquitetura de execução para múltiplos modos de interface.
- Criar bootstrap inicial para desktop e web.
- Documentar o roadmap e o changelog.
- Garantir que o projeto continue com testes básicos e execução estável.

## Sprint 2 — Interface web inicial
- Criar uma versão web simples do jogo com layout responsivo.
- Expor a mesma lógica de negócio já existente.
- Permitir seleção de dificuldade e início de partida.

## Sprint 3 — Experiência de jogo aprimorada
- Adicionar animações, feedback visual e estados de vitória/derrota mais ricos.
- Melhorar acesso por teclado e responsividade em telas menores.
- Refinar a linguagem visual para chamar atenção de perfis técnicos e entusiastas de jogos indie.

## Sprint 4 — Polish e portfólio
- Melhorar documentação técnica e visual.
- Adicionar screenshots e instruções de execução para cada interface.
- Preparar o repositório para apresentação profissional no GitHub.
- Definir o fluxo de deploy da camada web e da camada Java para ambientes gratuitos.

## Sprint 5 — Interações avançadas
- Expandir a entrada por teclado para oferecer uma experiência mais tradicional de jogo.
- Adicionar **indicador visual online/offline**: mostrar se a palavra veio do dicionário online ou local.
- Adicionar atalhos e reforços de usabilidade para partidas mais rápidas.
- Avaliar suporte a modos de jogo adicionais e feedback sonoro.

## Sprint 6 — Separação frontend/backend
- Mover a interface web para um projeto separado (Vercel).
- Manter apenas a API Java no backend (Render).
- Integração via CORS e configuração de endpoints remotos.

## Sprint 7 — Polimento final
- Adicionar sons e feedbacks hapticos (em dispositivos que suportam).
- Melhorar acessibilidade e suporte a temas escuro/claro.
- Preparar para publicação em app stores ou como PWA.
