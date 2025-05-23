package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import file.TaskManager;

import java.io.IOException;
import java.io.OutputStream;

public abstract class BaseHttpHandler implements HttpHandler {
    protected final TaskManager manager;
    public Gson gson;

    public BaseHttpHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
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

    protected void sendInternalServerError(HttpExchange exchange) throws IOException {
        String text = "Ошибка на стороне сервера. Попробуйте позже.";
        exchange.sendResponseHeaders(500, 0);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(text.getBytes());
        }
    }

    protected void sendBadRequest(HttpExchange exchange) throws IOException {
        String text = "Ошибка синтаксиса JSON.";
        exchange.sendResponseHeaders(400, 0);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(text.getBytes());
        }
    }
}
