package com.rapassos.forca.web;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

class GameWebServerTest {

    @Test
    void shouldServeHomepageWhenStarted() throws Exception {
        GameWebServer server = new GameWebServer(0);
        server.start();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + server.getPort() + "/")).GET().build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            assertTrue(response.statusCode() == 200);
            assertTrue(response.body().contains("Jogo da Forca"));
        } finally {
            server.stop();
        }
    }
}
