# Deployment e publicação

## Objetivo
Preparar a versão web do projeto para ser apresentada como um projeto de portfólio, com uma experiência visual forte e uma arquitetura simples de publicar.

## Estratégia recomendada
- Frontend web: Vercel
- Backend Java: Render ou Railway
- Alternativa simples: manter a camada web localmente e publicar apenas o frontend estático

## Opções de hospedagem

### Vercel
Ideal para a camada web, pois permite publicar a interface rapidamente e com excelente aparência para portfólio.

### Render
Boa opção para o backend Java, com suporte a aplicações Java e fácil integração via GitHub.

### Railway
Também é uma alternativa interessante para backend Java e deploy simples.

## Passo a passo sugerido
1. Criar uma pasta separada para o frontend estático.
2. Publicar o frontend no Vercel.
3. Publicar o backend Java em Render ou Railway.
4. Atualizar a interface para chamar o endpoint remoto do backend.
5. Registrar a URL pública no README e na documentação.
