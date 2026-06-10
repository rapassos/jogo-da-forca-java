package com.rapassos.forca.model;

public class Word {
    private final String text;
    private final String definition;

    public Word(String text, String definition) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("O texto da palavra não pode ser vazio.");
        }
        this.text = text.trim().toUpperCase();
        this.definition = definition != null ? definition.trim() : "Sem definição disponível.";
    }

    public String getText() {
        return text;
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return text;
    }
}
