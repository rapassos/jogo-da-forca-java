package com.rapassos.forca.model;

public class GameState {
    private final TargetWord targetWord;
    private final Difficulty difficulty;
    private String hiddenWord;
    private int currentErrors;

    public GameState(TargetWord targetWord, Difficulty difficulty) {
        this.targetWord = targetWord;
        this.difficulty = difficulty;
        this.currentErrors = 0;
        // Inicializa com traços baseados no tamanho real da palavra sorteada
        this.hiddenWord = "_".repeat(targetWord.getText().length());
    }

    public TargetWord getTargetWord() {
        return targetWord;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getHiddenWord() {
        return hiddenWord;
    }

    public int getCurrentErrors() {
        return currentErrors;
    }

    public void setHiddenWord(String hiddenWord) {
        this.hiddenWord = hiddenWord;
    }

    public void incrementErrors() {
        this.currentErrors++;
    }

    public boolean isGameOver() {
        return isWon() || currentErrors >= difficulty.getMaxErrors();
    }

    public boolean isWon() {
        return !hiddenWord.contains("_");
    }
}
