package com.rapassos.forca.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.Word;

public class DicionarioAbertoApiClient implements DictionaryService {


    private static final String API_RANDOM_URL = "https://api.dicionario-aberto.net/random";
    private static final String API_WORD_URL = "https://api.dicionario-aberto.net/word/";
    private static final int MAX_ATTEMPTS = 3;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final DictionaryService fallbackService;

    public DicionarioAbertoApiClient() {
        // Configura um cliente HTTP moderno com timeout de 4 segundos
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(4)).build();
        this.objectMapper = new ObjectMapper();
        this.fallbackService = new LocalFallbackService();
    }

    @Override
    public Word getRandomWord(Difficulty difficulty) {
        // Tentará obter uma palavra válida da API dentro do limite de tentativas
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                String rawWord = fetchRandomWordFromApi();

                if (isValidForDifficulty(rawWord, difficulty)) {
                    String definition = fetchDefinitionFromApi(rawWord);
                    System.out.println("[API] Palavra obtida com sucesso: " + rawWord);
                    return new Word(rawWord, definition);
                }
            } catch (Exception e) {
                System.err.println("[API] Falha na tentativa " + attempt + " ao conectar à API: "
                        + e.getMessage());
            }
        }

        // Se estourar as tentativas ou falhar a conexão, o Fallback assume silenciosamente
        System.out.println(
                "[FALLBACK] API indisponível ou critérios não atendidos. Ativando contingência local...");
        return fallbackService.getRandomWord(difficulty);
    }

    private String fetchRandomWordFromApi() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_RANDOM_URL))
                .header("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64) AppleWebKit/537.36")
                .header("Accept", "application/json").GET().build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "Erro HTTP na busca aleatória: Status " + response.statusCode());
        }

        String body = response.body().trim();

        // 🌟 Proteção defensiva: se começar com tag HTML, o servidor falhou na resposta
        if (body.startsWith("<")) {
            throw new RuntimeException(
                    "O servidor externo retornou uma página HTML/Erro em vez de dados JSON.");
        }

        JsonNode root = objectMapper.readTree(body);
        return root.get("word").asText();
    }

    private String fetchDefinitionFromApi(String word) throws Exception {
        String encodedWord = java.net.URLEncoder.encode(word.toLowerCase(),
                java.nio.charset.StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_WORD_URL + encodedWord))
                .header("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64) AppleWebKit/537.36")
                .header("Accept", "application/json").GET().build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return "Definição não encontrada na base externa.";
        }

        JsonNode root = objectMapper.readTree(response.body());

        // A API retorna um array de resultados. O significado real está envelopado em tags XML no
        // campo "xml".
        if (root.isArray() && !root.isEmpty()) {
            JsonNode firstEntry = root.get(0);
            if (firstEntry.has("xml")) {
                String rawXml = firstEntry.get("xml").asText();

                // 🌟 Mágica do Regex: Remove todas as tags HTML/XML (tudo entre < e >) e limpa os
                // espaços sobressalentes
                String cleanDefinition =
                        rawXml.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();

                return cleanDefinition.isEmpty() ? "Significado indisponível." : cleanDefinition;
            }
        }

        return "Significado indisponível para esta palavra.";
    }

    private boolean isValidForDifficulty(String word, Difficulty difficulty) {
        if (word == null || word.isBlank() || word.contains(" ") || word.contains("-")) {
            return false; // Evita palavras compostas capturadas pela API para simplificar o jogo
        }

        int length = word.length();
        return switch (difficulty) {
            case FACIL -> length >= 4 && length <= 6;
            case MEDIO -> length >= 7 && length <= 9;
            case DIFICIL -> length >= 10;
            case EXTREMO -> length >= 8; // No extremo, o desafio será o limite rígido de erros (3)
        };
    }
}
