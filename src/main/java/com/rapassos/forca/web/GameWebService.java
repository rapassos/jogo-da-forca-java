package com.rapassos.forca.web;

import com.rapassos.forca.controller.GameController;
import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.service.DictionaryService;

public class GameWebService {
    private final GameController controller;

    public GameWebService(DictionaryService dictionaryService) {
        this.controller = new GameController(dictionaryService);
    }

    public GameController startGame(Difficulty difficulty) {
        controller.startNewGame(difficulty);
        return controller;
    }
}
