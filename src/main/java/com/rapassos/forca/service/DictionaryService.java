package com.rapassos.forca.service;

import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.TargetWord;

public interface DictionaryService {
    TargetWord getRandomWord(Difficulty difficulty);
}
