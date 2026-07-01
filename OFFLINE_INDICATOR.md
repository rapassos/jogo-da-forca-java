# Feature: Indicador Online/Offline

## Descrição
Adicionar um indicador visual que mostre se a palavra atual veio do dicionário online (API Dicionário Aberto) ou do dicionário local (fallback em JSON).

## Motivação
- **Transparência:** o usuário saberá se está jogando com uma palavra de uma fonte oficial ou do fallback local.
- **Confiabilidade:** reforça a confiança na qualidade da palavra.
- **Portfolio:** demonstra atenção aos detalhes e boas práticas de UX.

## Especificação Técnica

### Backend (Java)
1. Adicionar um campo `source` no `TargetWord` e `GameState`:
   ```java
   enum WordSource { ONLINE, OFFLINE }
   
   private WordSource source;
   ```

2. Modificar `DicionarioAbertoApiClient` para retornar `ONLINE` na construção de `TargetWord`.
3. Modificar `LocalFallbackService` para retornar `OFFLINE` na construção de `TargetWord`.

4. Incluir `source` no ViewModel retornado pela API:
   ```json
   {
     "source": "ONLINE",
     "word": "exemplo",
     ...
   }
   ```

### Frontend (JavaScript/HTML)
1. Criar um elemento `<span id="sourceIndicator">` na interface.
2. Atualizar no método `renderState()`:
   ```javascript
   const sourceLabel = state.source === 'ONLINE' ? '📡 Online' : '📖 Offline';
   sourceIndicator.textContent = sourceLabel;
   ```

3. Estilizar com cores diferentes:
   - **Online**: cor verde ou azul
   - **Offline**: cor âmbar ou cinza

### UI/UX
- Posicionar o indicador próximo ao status da partida.
- Usar ícones (📡 para online, 📖 para offline) para melhor clareza.
- Adicionar tooltip explicando o significado.

## Exemplo de Output
```
Status: Em andamento
Dificuldade: Médio
Erros: 2 / 5
Fonte: 📡 Online (Dicionário Aberto)
```

## Testes
- [ ] Palavra do online aparece com indicador correto
- [ ] Palavra do offline aparece com indicador correto
- [ ] Indicador é exibido durante toda a partida
- [ ] Indicador não interfere com a responsividade

## Sprint
- Planejado para **Sprint 5** — Interações avançadas
