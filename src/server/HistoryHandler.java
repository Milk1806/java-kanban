package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import file.TaskManager;

import java.io.IOException;

class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!(manager.getHistory().isEmpty())) {
                String response = gson.toJson(manager.getHistory());
                sendText(exchange, response);
            } else {
                sendNotFound(exchange, "История просмотров пуста.");
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }
}
