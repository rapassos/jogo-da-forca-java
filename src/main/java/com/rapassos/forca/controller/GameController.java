package com.rapassos.forca.controller;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;
import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.GameState;
import com.rapassos.forca.model.TargetWord;
import com.rapassos.forca.service.DictionaryService;

public class GameController {
    private final DictionaryService dictionaryService;
    private GameState currentState;
    private Set<Character> guessedLetters;

    public GameController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
        this.guessedLetters = new HashSet<>();
    }

    public void startNewGame(Difficulty difficulty) {
        TargetWord word = dictionaryService.getRandomWord(difficulty);
        this.currentState = new GameState(word, difficulty);
        this.guessedLetters.clear();
    }

    public boolean makeGuess(char letter) {
        letter = Character.toUpperCase(letter);
        if (guessedLetters.contains(letter) || currentState.isGameOver()) {
            return false;
        }

        guessedLetters.add(letter);
        String normalizedTarget = normalize(currentState.getTargetWord().getText());
        boolean hit = normalizedTarget.contains(String.valueOf(letter));

        if (hit) {
            updateHiddenWord(letter, normalizedTarget);
        } else {
            currentState.incrementErrors();
        }

        return hit;
    }

    private void updateHiddenWord(char letter, String normalizedTarget) {
        StringBuilder sb = new StringBuilder(currentState.getHiddenWord());
        String originalWord = currentState.getTargetWord().getText().toUpperCase();

        for (int i = 0; i < normalizedTarget.length(); i++) {
            if (normalizedTarget.charAt(i) == letter) {
                sb.setCharAt(i, originalWord.charAt(i));
            }
        }
        currentState.setHiddenWord(sb.toString());
    }

    private String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{M}", "")
                .toUpperCase();
    }

    public GameState getCurrentState() {
        return currentState;
    }
}
