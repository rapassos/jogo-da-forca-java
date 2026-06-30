package com.rapassos.forca.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AppBootstrapTest {

    @Test
    void shouldDefaultToDesktopWhenNoModeProvided() {
        assertEquals(AppMode.DESKTOP, AppBootstrap.resolveMode(new String[0]));
    }

    @Test
    void shouldResolveWebMode() {
        assertEquals(AppMode.WEB, AppBootstrap.resolveMode(new String[] {"--mode=web"}));
    }

    @Test
    void shouldRejectUnknownMode() {
        assertThrows(IllegalArgumentException.class,
                () -> AppBootstrap.resolveMode(new String[] {"--mode=mobile"}));
    }
}
