package com.rapassos.forca.model;

public enum Difficulty {
    FACIL(8, "Fácil (Palavras Curtas)"),
    MEDIO(6, "Médio (Palavras Médias)"),
    DIFICIL(5, "Difícil (Palavras Longas)"),
    EXTREMO(3, "Extremo (Palavras Raras)");

    private final int maxErrors;
    private final String description;

    Difficulty(int maxErrors, String description) {
        this.maxErrors = maxErrors;
        this.description = description;
    }

    public int getMaxErrors() {
        return maxErrors;
    }

    public String getDescription() {
        return description;
    }
}