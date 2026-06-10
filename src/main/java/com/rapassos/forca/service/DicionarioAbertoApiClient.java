package com.rapassos.forca.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.TargetWord;

public class DicionarioAbertoApiClient implements DictionaryService {
    private static final String API_RANDOM_URL = "https://api.dicionario-aberto.net/random";
    private static final String API_WORD_URL = "https://api.dicionario-aberto.net/word/";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DicionarioAbertoApiClient() {
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public TargetWord getRandomWord(Difficulty difficulty) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_RANDOM_URL))
                    .header("User-Agent", "Mozilla/5.0").header("Accept", "application/json").GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("API indisponível");
            }

            JsonNode root = objectMapper.readTree(response.body());
            String word = root.get("word").asText();

            // 🛠️ Valida o tamanho E rejeita palavras que contenham hífen
            if (!difficulty.isValidLength(word.length()) || word.contains("-")) {
                throw new RuntimeException("Palavra inadequada (tamanho inválido ou possui hífen)");
            }

            String definition = fetchDefinitionFromApi(word);
            return new TargetWord(word, definition);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao consumir API: " + e.getMessage());
        }
    }

    private String fetchDefinitionFromApi(String word) throws Exception {
        String encodedWord = java.net.URLEncoder.encode(word.toLowerCase(),
                java.nio.charset.StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_WORD_URL + encodedWord))
                .header("User-Agent", "Mozilla/5.0").header("Accept", "application/json").GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            return "Definição não encontrada na base externa.";
        }

        JsonNode root = objectMapper.readTree(response.body());
        if (root.isArray() && !root.isEmpty()) {
            JsonNode firstEntry = root.get(0);
            if (firstEntry.has("xml")) {
                String rawXml = firstEntry.get("xml").asText();
                String cleanDefinition =
                        rawXml.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
                return cleanDefinition.isEmpty() ? "Significado indisponível." : cleanDefinition;
            }
        }
        return "Significado indisponível para esta palavra.";
    }
}
