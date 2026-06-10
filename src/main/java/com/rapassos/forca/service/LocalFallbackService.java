package com.rapassos.forca.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.TargetWord;

public class LocalFallbackService implements DictionaryService { // 🛠️ Assinando o contrato da
                                                                 // interface
    private final ObjectMapper mapper = new ObjectMapper();
    private final Random random = new Random();
    private JsonNode rootNode;

    public LocalFallbackService() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("palavras.json")) {
            if (is == null) {
                throw new RuntimeException("Arquivo palavras.json não encontrado nos recursos.");
            }
            this.rootNode = mapper.readTree(is);
        } catch (Exception e) {
            System.err.println("[FALLBACK] Erro ao carregar dicionário local: " + e.getMessage());
        }
    }

    @Override // 🛠️ Alinhado com o nome exato que o GameController está chamando
    public TargetWord getRandomWord(Difficulty difficulty) {
        String key = difficulty.name().toLowerCase();
        List<TargetWord> validWords = new ArrayList<>();

        if (rootNode != null && rootNode.has(key)) {
            for (JsonNode node : rootNode.get(key)) {
                String word = node.get("word").asText();
                String def = node.get("definition").asText();

                // 🛠️ Validação dupla: tamanho correto E sem hífens
                if (difficulty.isValidLength(word.length()) && !word.contains("-")) {
                    validWords.add(new TargetWord(word, def));
                }
            }
        }

        // Contingência se a lista filtrada falhar
        if (validWords.isEmpty()) {
            return new TargetWord("java", "Linguagem de programação orientada a objetos.");
        }

        return validWords.get(random.nextInt(validWords.size()));
    }
}
