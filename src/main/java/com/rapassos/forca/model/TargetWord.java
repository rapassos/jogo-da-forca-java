package com.rapassos.forca.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TargetWord {
    @JsonProperty("word")
    private String text;
    private String definition;

    public TargetWord() {}

    public TargetWord(String text, String definition) {
        this.text = text;
        this.definition = definition;
    }

    public String getText() { return text; }
    public String getDefinition() { return definition; }
    
    @Override
    public String toString() {
        return text.toUpperCase();
    }
}