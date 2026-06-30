package com.rapassos.forca.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapassos.forca.controller.GameController;
import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.GameState;
import com.rapassos.forca.service.LocalFallbackService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class GameWebServer {
    private final HttpServer server;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AtomicReference<GameController> activeController = new AtomicReference<>();
    private final GameWebService webService = new GameWebService(new LocalFallbackService());

    public GameWebServer(int port) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.createContext("/", this::handleRoot);
        this.server.createContext("/api/start", this::handleStart);
        this.server.createContext("/api/guess", this::handleGuess);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    public int getPort() {
        return server.getAddress().getPort();
    }

    private void handleRoot(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Método não permitido");
            return;
        }

        byte[] response = loadResource("web/index.html");
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        sendBytes(exchange, 200, response);
    }

    private void handleStart(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Método não permitido");
            return;
        }

        Map<String, Object> payload = readJson(exchange);
        Difficulty difficulty = Difficulty
                .valueOf(((String) payload.getOrDefault("difficulty", "FACIL")).toUpperCase());
        GameController controller = webService.startGame(difficulty);
        activeController.set(controller);
        sendJson(exchange, 200, toViewModel(controller.getCurrentState()));
    }

    private void handleGuess(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Método não permitido");
            return;
        }

        GameController controller = activeController.get();
        if (controller == null) {
            controller = webService.startGame(Difficulty.FACIL);
            activeController.set(controller);
        }

        Map<String, Object> payload = readJson(exchange);
        String letter = String.valueOf(payload.getOrDefault("letter", "")).trim();
        if (letter.isEmpty()) {
            sendText(exchange, 400, "Informe uma letra válida");
            return;
        }

        controller.makeGuess(letter.toUpperCase().charAt(0));
        sendJson(exchange, 200, toViewModel(controller.getCurrentState()));
    }

    private Map<String, Object> toViewModel(GameState state) {
        Map<String, Object> viewModel = new LinkedHashMap<>();
        viewModel.put("difficulty", state.getDifficulty().getDescription());
        viewModel.put("difficultyKey", state.getDifficulty().name().toLowerCase());
        viewModel.put("hiddenWord", state.getHiddenWord());
        viewModel.put("currentErrors", state.getCurrentErrors());
        viewModel.put("maxErrors", state.getDifficulty().getMaxErrors());
        viewModel.put("gameOver", state.isGameOver());
        viewModel.put("won", state.isWon());
        viewModel.put("word", state.getTargetWord().getText());
        viewModel.put("definition", state.getTargetWord().getDefinition());
        return viewModel;
    }

    private Map<String, Object> readJson(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (body.isBlank()) {
            return Map.of();
        }
        return objectMapper.readValue(body, Map.class);
    }

    private byte[] loadResource(String path) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + path);
            }
            return inputStream.readAllBytes();
        }
    }

    private void sendJson(HttpExchange exchange, int statusCode, Object payload)
            throws IOException {
        byte[] responseBody = objectMapper.writeValueAsBytes(payload);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        sendBytes(exchange, statusCode, responseBody);
    }

    private void sendText(HttpExchange exchange, int statusCode, String message)
            throws IOException {
        sendBytes(exchange, statusCode, message.getBytes(StandardCharsets.UTF_8));
    }

    private void sendBytes(HttpExchange exchange, int statusCode, byte[] responseBody)
            throws IOException {
        exchange.sendResponseHeaders(statusCode, responseBody.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBody);
        }
    }
}
