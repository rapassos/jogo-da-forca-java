package com.rapassos.forca.service;

import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.Word;

public interface DictionaryService {
    /**
     * Obtém uma palavra aleatória com base na dificuldade escolhida.
     */
    Word getRandomWord(Difficulty difficulty);
}
