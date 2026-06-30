package com.rapassos.forca.app;

import java.io.IOException;
import javax.swing.SwingUtilities;
import com.rapassos.forca.view.MenuView;
import com.rapassos.forca.web.GameWebServer;

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
            try {
                GameWebServer server = new GameWebServer(8080);
                server.start();
                System.out.println("Servidor web iniciado em http://localhost:8080/");
                System.out.println("Pressione Ctrl+C para encerrar.");
                Thread.currentThread().join();
            } catch (IOException e) {
                throw new RuntimeException("Falha ao iniciar o servidor web", e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return;
        }

        SwingUtilities.invokeLater(() -> {
            MenuView menu = new MenuView();
            menu.setVisible(true);
        });
    }
}
