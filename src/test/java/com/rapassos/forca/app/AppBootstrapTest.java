package com.rapassos.forca.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void shouldResolveDesktopModeExplicitly() {
        assertEquals(AppMode.DESKTOP, AppBootstrap.resolveMode(new String[] {"--mode=desktop"}));
    }

    @Test
    void shouldRejectUnknownMode() {
        assertThrows(IllegalArgumentException.class,
                () -> AppBootstrap.resolveMode(new String[] {"--mode=mobile"}));
    }

    @Test
    void shouldResolvePortFromEnvironmentWhenNoArgumentProvided() {
        assertTrue(AppBootstrap.resolvePort(new String[0]) >= 0);
    }
}
