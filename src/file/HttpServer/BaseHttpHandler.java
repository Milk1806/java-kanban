package file.HttpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import file.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler implements HttpHandler {
    protected final TaskManager manager;
    Gson gson;

    public BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(text.getBytes());
            }
        } else if (exchange.getRequestMethod().equals("POST")) {
            exchange.sendResponseHeaders(201, 0);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(text.getBytes());
            }
        } else if (exchange.getRequestMethod().equals("DELETE")) {
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(text.getBytes());
            }
        }
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(text.getBytes());
        }
    }

    protected void sendHasInteractions(HttpExchange exchange, String text) throws IOException {
        exchange.sendResponseHeaders(406, 0);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(text.getBytes());
        }
    }
}
