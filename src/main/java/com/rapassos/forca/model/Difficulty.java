package com.rapassos.forca.model;

public enum Difficulty {
    FACIL("Fácil (Palavras Curtas)", 6, 3, 5), MEDIO("Médio (Palavras Médias)", 5, 6, 8), DIFICIL(
            "Difícil (Palavras Longas)", 4, 9,
            11), EXTREMO("Extremo (Palavras Complexas)", 3, 12, 30);

    private final String description;
    private final int maxErrors;
    private final int minLength;
    private final int maxLength;

    Difficulty(String description, int maxErrors, int minLength, int maxLength) {
        this.description = description;
        this.maxErrors = maxErrors;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxErrors() {
        return maxErrors;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public boolean isValidLength(int length) {
        return length >= minLength && length <= maxLength;
    }
}
