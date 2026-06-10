package com.rapassos.forca.model;

public class TargetWord {
    private final String text;
    private final String definition;

    public TargetWord(String text, String definition) {
        this.text = text;
        this.definition = definition;
    }

    public String getText() {
        return text;
    }

    public String getDefinition() {
        return definition;
    }
}
