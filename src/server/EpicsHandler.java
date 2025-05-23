package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import file.TaskManager;
import task.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] array = exchange.getRequestURI().getPath().split("/");
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGetRequest(exchange, array);
                break;
            case "POST":
                handlePostRequest(exchange, array);
                break;
            case "DELETE":
                handleDeleteRequest(exchange, array);
                break;
            default:
                ;
        }
    }

    private void handleGetRequest(HttpExchange exchange, String[] array) throws IOException {
        try {
            if (array.length == 2) {
                String response = gson.toJson(manager.getEpics());
                sendText(exchange, response);
            } else if (array.length == 3) {
                int id = Integer.parseInt(array[2]);
                if (manager.getEpicByld(id).isPresent()) {
                    String response = gson.toJson(manager.getEpicByld(id).get());
                    sendText(exchange, response);
                } else {
                    sendNotFound(exchange, "Эпическая задача не найдена.");
                }
            } else if (array.length == 4) {
                int id = Integer.parseInt(array[2]);
                if (manager.getEpicByld(id).isPresent()) {
                    String response = gson.toJson(manager.getSubtasksOfEpic(id).get());
                    sendText(exchange, response);
                } else {
                    sendNotFound(exchange, "Эпическая задача не найдена.");
                }
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void handlePostRequest(HttpExchange exchange, String[] array) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            try {
                Epic epic = gson.fromJson(body, Epic.class);
                if (array.length == 2) {
                    manager.addEpic(epic);
                    if (manager.getEpics().containsKey(epic.getID())) {
                        sendText(exchange, "Эпическая задача добавлена");
                    } else {
                        sendHasInteractions(exchange, """
                        Эпическая задача пересекается по времени выполнения с другой задачей.
                        """);
                    }
                } else if (array.length == 3) {
                    int id = Integer.parseInt(array[2]);
                    if (manager.getEpics().containsKey(id) && manager.intersectionOfTasks(epic)) {
                        manager.updateEpic(epic);
                        sendText(exchange, "Эпическая задача обновлена.");
                    } else if (!(manager.getEpics().containsKey(id))) {
                        sendNotFound(exchange, "Эпическая задача не найдена.");
                    } else {
                        sendHasInteractions(exchange, """
                        Эпическая задача пересекается по времени выполнения с другой задачей.
                        """);
                    }
                }
            } catch (JsonSyntaxException e) {
                sendBadRequest(exchange);
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void handleDeleteRequest(HttpExchange exchange, String[] array) throws IOException {
        try {
            int id = Integer.parseInt(array[2]);
            if (manager.getEpics().containsKey(id)) {
                manager.removeEpicOnID(id);
                sendText(exchange, "Эпическая задача удалена.");
            } else {
                sendNotFound(exchange, "Эпическая задача не найдена.");
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }
}
