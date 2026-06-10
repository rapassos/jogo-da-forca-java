package com.rapassos.forca.model;

import java.util.HashSet;
import java.util.Set;

public class GameState {
    private final Word targetWord;
    private final Difficulty difficulty;
    private final Set<Character> guessedLetters;
    private int currentErrors;

    public GameState(Word targetWord, Difficulty difficulty) {
        if (targetWord == null || difficulty == null) {
            throw new IllegalArgumentException("Palavra e dificuldade não podem ser nulas.");
        }
        this.targetWord = targetWord;
        this.difficulty = difficulty;
        this.guessedLetters = new HashSet<>();
        this.currentErrors = 0;
    }

    /**
     * Processa o palpite de uma letra enviado pelo jogador.
     * 
     * @param letter Letra palpitada.
     * @return true se a letra existir na palavra secreta, false caso contrário.
     */
    public boolean guessLetter(char letter) {
        char normalizedLetter = Character.toUpperCase(letter);

        // Se o jogo já acabou ou a letra já foi tentada, ignora
        if (isGameOver() || guessedLetters.contains(normalizedLetter)) {
            return false;
        }

        guessedLetters.add(normalizedLetter);

        // Se a palavra NÃO contém a letra, contabiliza o erro
        if (targetWord.getText().indexOf(normalizedLetter) == -1) {
            currentErrors++;
            return false;
        }

        return true;
    }

    /**
     * Gera a representação visual da palavra para a tela (ex: J _ V _).
     */
    public String getHiddenWord() {
        StringBuilder sb = new StringBuilder();
        String text = targetWord.getText();

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);

            // Ignora espaços ou hifens na validação da forca se a API contiver palavras compostas
            if (letter == ' ' || letter == '-') {
                sb.append(letter).append(" ");
            } else if (guessedLetters.contains(letter)) {
                sb.append(letter).append(" ");
            } else {
                sb.append("_ ");
            }
        }
        return sb.toString().trim();
    }

    public boolean isWon() {
        // O jogador ganha se não houver mais nenhum caractere escondido por '_'
        return !getHiddenWord().contains("_");
    }

    public boolean isLost() {
        return currentErrors >= difficulty.getMaxErrors();
    }

    public boolean isGameOver() {
        return isWon() || isLost();
    }

    // Getters para uso da Interface Gráfica (View) futuramente
    public Word getTargetWord() {
        return targetWord;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getCurrentErrors() {
        return currentErrors;
    }

    public Set<Character> getGuessedLetters() {
        return guessedLetters;
    }
}
