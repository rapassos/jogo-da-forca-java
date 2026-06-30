package com.rapassos.forca.app;

import javax.swing.SwingUtilities;
import com.rapassos.forca.service.LocalFallbackService;
import com.rapassos.forca.view.MenuView;
import com.rapassos.forca.web.GameWebService;

public class AppBootstrap {

    public static AppMode resolveMode(String[] args) {
        if (args == null || args.length == 0) {
            return AppMode.DESKTOP;
        }

        for (String arg : args) {
            if ("--mode=web".equalsIgnoreCase(arg)) {
                return AppMode.WEB;
            }
            if ("--mode=desktop".equalsIgnoreCase(arg)) {
                return AppMode.DESKTOP;
            }
        }

        throw new IllegalArgumentException(
                "Modo de execução inválido. Use --mode=desktop ou --mode=web");
    }

    public static void launch(String[] args) {
        AppMode mode = resolveMode(args);

        if (mode == AppMode.WEB) {
            GameWebService webService = new GameWebService(new LocalFallbackService());
            System.out.println(
                    "Modo web inicial pronto. Serviço de jogo disponível para a próxima etapa da interface.");
            System.out.println("Estado inicial: "
                    + webService.startGame(com.rapassos.forca.model.Difficulty.FACIL)
                            .getCurrentState().getDifficulty().getDescription());
            return;
        }

        SwingUtilities.invokeLater(() -> {
            MenuView menu = new MenuView();
            menu.setVisible(true);
        });
    }
}
