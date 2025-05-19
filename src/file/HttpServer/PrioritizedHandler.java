package file.HttpServer;

import com.sun.net.httpserver.HttpExchange;
import file.TaskManager;

import java.io.IOException;

class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = gson.toJson(manager.getPrioritizedTasks());
        if (response.isEmpty()) {
            sendNotFound(exchange, "Список отсортированных задач по времени выполнения пуст.");
        } else {
            sendText(exchange, response);
        }
    }
}
