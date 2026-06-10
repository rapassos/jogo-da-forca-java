package com.rapassos.forca.controller;

import java.text.Normalizer;
import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.GameState;
import com.rapassos.forca.model.TargetWord;
import com.rapassos.forca.service.DicionarioAbertoApiClient;
import com.rapassos.forca.service.LocalFallbackService;

public class GameController {
    private final DicionarioAbertoApiClient apiClient;
    private final LocalFallbackService fallbackService;
    private GameState currentState;

    public GameController() {
        this.apiClient = new DicionarioAbertoApiClient();
        this.fallbackService = new LocalFallbackService();
    }

    public void startNewGame(Difficulty difficulty) {
        TargetWord selectedWord = null;
        int attempts = 0;

        // Tenta buscar da API uma palavra que atenda ao tamanho da dificuldade
        while (attempts < 3) {
            try {
                selectedWord = apiClient.getRandomWord(difficulty);
                if (selectedWord != null) {
                    break;
                }
            } catch (Exception e) {
                // Avança na tentativa silenciosamente em caso de timeout/tamanho inválido
            }
            attempts++;
        }

        // Se a API falhar ou não encontrar o tamanho correto, usa o banco local filtrado
        if (selectedWord == null) {
            selectedWord = fallbackService.getRandomWord(difficulty);
        }

        this.currentState = new GameState(selectedWord, difficulty);
    }

    public boolean makeGuess(char letter) {
        if (currentState == null || currentState.isGameOver())
            return false;

        String target = currentState.getTargetWord().getText().toLowerCase();
        StringBuilder hidden = new StringBuilder(currentState.getHiddenWord());
        boolean hit = false;

        char normalizedGuess = normalizeChar(letter);

        for (int i = 0; i < target.length(); i++) {
            if (normalizeChar(target.charAt(i)) == normalizedGuess) {
                hidden.setCharAt(i, target.charAt(i)); // Revela o caractere original (com acento!)
                hit = true;
            }
        }

        currentState.setHiddenWord(hidden.toString());

        if (!hit) {
            currentState.incrementErrors();
        }

        return hit;
    }

    private char normalizeChar(char c) {
        String normalized = Normalizer.normalize(String.valueOf(c), Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", ""); // Remove marcas de acentuação
        return normalized.isEmpty() ? Character.toLowerCase(c)
                : Character.toLowerCase(normalized.charAt(0));
    }

    public GameState getCurrentState() {
        return currentState;
    }
}
