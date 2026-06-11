package com.rapassos.forca.model;

public enum Difficulty {
    FACIL("facil", "Fácil (3-5 letras)", 6, 3, 5), MEDIO("medio", "Médio (6-8 letras)", 5, 6,
            8), DIFICIL("dificil", "Difícil (9-11 letras)", 4, 9,
                    11), EXTREMO("extremo", "Extremo (12+ letras)", 3, 12, 30);

    private final String key;
    private final String description;
    private final int maxErrors;
    private final int minLength;
    private final int maxLength;

    Difficulty(String key, String description, int maxErrors, int minLength, int maxLength) {
        this.key = key;
        this.description = description;
        this.maxErrors = maxErrors;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public String getKey() {
        return key;
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
