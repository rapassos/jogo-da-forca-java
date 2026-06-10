package com.rapassos.forca.controller;

import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.GameState;
import com.rapassos.forca.model.Word;
import com.rapassos.forca.service.DicionarioAbertoApiClient;
import com.rapassos.forca.service.DictionaryService;

public class GameController {
    private final DictionaryService dictionaryService;
    private GameState currentState;

    public GameController() {
        // Injeta a API cliente que já possui o fallback local embutido
        this.dictionaryService = new DicionarioAbertoApiClient();
    }

    /**
     * Inicializa uma nova rodada do jogo obtendo uma palavra adequada à dificuldade.
     */
    public void startNewGame(Difficulty difficulty) {
        Word selectedWord = dictionaryService.getRandomWord(difficulty);
        this.currentState = new GameState(selectedWord, difficulty);
    }

    /**
     * Repassa o palpite da letra para a validação no motor lógico.
     * 
     * @return true se acertou a letra, false se errou.
     */
    public boolean makeGuess(char letter) {
        if (currentState == null) {
            throw new IllegalStateException("Nenhuma partida foi inicializada.");
        }
        return currentState.guessLetter(letter);
    }

    /**
     * Expõe o estado atual da partida para que a View (Interface) possa se desenhar.
     */
    public GameState getCurrentState() {
        return currentState;
    }
}
