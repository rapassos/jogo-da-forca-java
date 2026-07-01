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

    public static int resolvePort(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if ("--port".equalsIgnoreCase(args[i]) && i + 1 < args.length) {
                    return Integer.parseInt(args[i + 1]);
                }
                if (args[i].startsWith("--port=")) {
                    return Integer.parseInt(args[i].substring("--port=".length()));
                }
            }
        }

        String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.isBlank()) {
            return Integer.parseInt(envPort);
        }

        return 8080;
    }

    public static void launch(String[] args) {
        AppMode mode = resolveMode(args);

        if (mode == AppMode.WEB) {
            try {
                int port = resolvePort(args);
                GameWebServer server = new GameWebServer(port);
                server.start();
                System.out.println("Servidor web iniciado em http://0.0.0.0:" + port + "/");
                System.out.println("Pressione Ctrl+C para encerrar.");
                Thread.currentThread().join();
            } catch (IOException e) {
                throw new RuntimeException("Falha ao iniciar o servidor web", e);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("A porta informada é inválida.", e);
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
