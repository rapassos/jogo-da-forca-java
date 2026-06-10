package com.rapassos.forca.main;

import javax.swing.SwingUtilities;
import com.rapassos.forca.view.MenuView;

public class Main {
    public static void main(String[] args) {
        // Inicializa a interface a partir do Menu Inicial
        SwingUtilities.invokeLater(() -> {
            MenuView menu = new MenuView();
            menu.setVisible(true);
        });
    }
}
