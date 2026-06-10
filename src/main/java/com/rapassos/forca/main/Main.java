package com.rapassos.forca.main;

import javax.swing.SwingUtilities;
import com.rapassos.forca.view.GameView;

public class Main {
    public static void main(String[] args) {
        // Inicializa a interface Swing de forma thread-safe
        SwingUtilities.invokeLater(() -> {
            GameView game = new GameView();
            game.setVisible(true);
        });
    }
}
