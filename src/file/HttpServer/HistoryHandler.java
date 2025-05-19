package file.HttpServer;

import com.sun.net.httpserver.HttpExchange;
import file.TaskManager;

import java.io.IOException;

class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!(manager.getHistory().isEmpty())) {
            String response = gson.toJson(manager.getHistory());
            sendText(exchange, response);
        } else {
            sendNotFound(exchange, "История просмотров пуста.");
        }
    }
}
