package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import file.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager manager, Gson gson) {
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
        }
    }

    private void handleGetRequest(HttpExchange exchange, String[] array) throws IOException {
        try {
            if (array.length == 2) {
                String response = gson.toJson(manager.getTasks());
                sendText(exchange, response);
            } else if (array.length == 3) {
                int id = Integer.parseInt(array[2]);
                if (manager.getTaskByld(id).isPresent()) {
                    String response = gson.toJson(manager.getTaskByld(id).get());
                    sendText(exchange, response);
                } else if (manager.getTaskByld(id).isEmpty()) {
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
                Task task = gson.fromJson(body, Task.class);
                if (array.length == 2) {
                    manager.addTask(task);
                    if (manager.getTasks().containsKey(task.getID())) {
                        sendText(exchange, "Задача добавлена");
                    } else {
                        sendHasInteractions(exchange, "Задача пересекается по времени выполнения с другой задачей.");
                    }
                } else if (array.length == 3) {
                    int id = Integer.parseInt(array[2]);
                    if (manager.getTasks().containsKey(id) && manager.intersectionOfTasks(task)) {
                        manager.updateTask(task);
                        sendText(exchange, "Задача обновлена.");
                    } else if (!(manager.getTasks().containsKey(id))) {
                        sendNotFound(exchange, "Задача не найдена.");
                    } else {
                        sendHasInteractions(exchange, "Задача пересекается по времени выполнения с другой задачей.");
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
            if (manager.getTasks().containsKey(id)) {
                manager.removeTaskOnID(id);
                sendText(exchange, "Задача удалена.");
            } else if (!(manager.getTasks().containsKey(id))){
                sendNotFound(exchange, "Задача не найдена.");
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }
}
