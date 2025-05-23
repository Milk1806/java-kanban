package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import file.TaskManager;
import task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class SubtasksHandler extends BaseHttpHandler {

    public SubtasksHandler(TaskManager manager, Gson gson) {
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
                String response = gson.toJson(manager.getSubtasks());
                sendText(exchange, response);
            } else if (array.length == 3) {
                int id = Integer.parseInt(array[2]);
                if (manager.getSubtaskByld(id).isPresent()) {
                    String response = gson.toJson(manager.getSubtaskByld(id).get());
                    sendText(exchange, response);
                } else {
                    sendNotFound(exchange, "Задача не найдена.");
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
                Subtask subtask = gson.fromJson(body, Subtask.class);
                if (array.length == 2) {
                    manager.addSubtask(subtask);
                    if (manager.getEpicByld(subtask.getEpicId()).isEmpty()) {
                        sendNotFound(exchange, "Эпическая задача подзадачи не найдена. Подзадачу невозможно добавить");
                    } else if (manager.getSubtasks().containsKey(subtask.getID())) {
                        sendText(exchange, "Подзадача добавлена");
                    } else {
                        sendHasInteractions(exchange, "Подзадача пересекается по времени выполнения с другой задачей.");
                    }
                } else if (array.length == 3) {
                    int id = Integer.parseInt(array[2]);
                    if (manager.getSubtasks().containsKey(id) && manager.intersectionOfTasks(subtask)) {
                        manager.updateSubtask(subtask);
                        sendText(exchange, "Подзадача обновлена.");
                    } else if (!(manager.getSubtasks().containsKey(id))) {
                        sendNotFound(exchange, "Подзадача не найдена.");
                    } else {
                        sendHasInteractions(exchange, "Подзадача пересекается по времени выполнения с другой задачей.");
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
            if (manager.getSubtasks().containsKey(id)) {
                manager.removeSubtaskOnID(id);
                sendText(exchange, "Подзадача удалена.");
            } else {
                sendNotFound(exchange, "Подзадача не найдена.");
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }
}
