package com.rapassos.forca.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.service.LocalFallbackService;

class GameWebServiceTest {

    @Test
    void shouldCreateControllerWithSelectedDifficulty() {
        GameWebService service = new GameWebService(new LocalFallbackService());

        var controller = service.startGame(Difficulty.FACIL);

        assertNotNull(controller);
        assertEquals(Difficulty.FACIL, controller.getCurrentState().getDifficulty());
        assertFalse(controller.getCurrentState().isGameOver());
    }
}
