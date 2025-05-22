package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import file.TaskManager;

import java.io.IOException;

class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String response = gson.toJson(manager.getPrioritizedTasks());
            if (response.isEmpty()) {
                sendNotFound(exchange, "Список отсортированных задач по времени выполнения пуст.");
            } else {
                sendText(exchange, response);
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }
}
