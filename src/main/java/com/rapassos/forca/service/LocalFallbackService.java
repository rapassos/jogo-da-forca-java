package com.rapassos.forca.service;

import java.io.InputStream;
import java.util.Random;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.Word;

public class LocalFallbackService implements DictionaryService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    @Override
    public Word getRandomWord(Difficulty difficulty) {
        try (InputStream is =
                getClass().getClassLoader().getResourceAsStream("words_fallback.json")) {
            if (is == null) {
                throw new IllegalStateException(
                        "Arquivo words_fallback.json não encontrado nos recursos.");
            }

            JsonNode rootNode = objectMapper.readTree(is);
            JsonNode wordsNode = rootNode.get(difficulty.name());

            if (wordsNode == null || !wordsNode.isArray() || wordsNode.isEmpty()) {
                return getHardcodedEmergencyWord(difficulty);
            }

            // Sorteia um dos elementos do array JSON correspondente à dificuldade
            int randomIndex = random.nextInt(wordsNode.size());
            JsonNode selectedWordNode = wordsNode.get(randomIndex);

            String text = selectedWordNode.get("text").asText();
            String definition = selectedWordNode.get("definition").asText();

            return new Word(text, definition);

        } catch (Exception e) {
            // Se até a leitura do arquivo local falhar, aciona a última linha de defesa
            System.err.println("Erro ao ler fallback local: " + e.getMessage());
            return getHardcodedEmergencyWord(difficulty);
        }
    }

    /**
     * Última linha de defesa (Hardcoded) caso ocorra falha catastrófica de I/O.
     */
    private Word getHardcodedEmergencyWord(Difficulty difficulty) {
        return switch (difficulty) {
            case FACIL -> new Word("TESTE", "Ação de testar algo para verificar sua eficácia.");
            case MEDIO -> new Word("CODIGO",
                    "Conjunto de regras ou instruções escritas em uma linguagem específica.");
            case DIFICIL -> new Word("COMPILADOR",
                    "Programa que traduz código-fonte em linguagem de máquina.");
            case EXTREMO -> new Word("RECURSAO",
                    "Método de programação no qual uma função chama a si mesma.");
        };
    }
}
