package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import file.Managers;
import file.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrioritizedHandlerTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public PrioritizedHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.clearTasks();
        manager.clearSubtasks();
        manager.clearEpics();
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    @Test
    public void getPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task(manager.getNewID(), "123", "1", TaskStatus.NEW, "2025-05-01T12:00", 60);
        Task task2 = new Task(manager.getNewID(), "123124", "1", TaskStatus.NEW, "2025-05-01T10:00", 60);
        manager.addTask(task1);
        manager.addTask(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> prioritizedTasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertEquals(2, prioritizedTasks.size(), "Отсортированный список по времени выполнения отображается не корректно.");
        assertEquals(task2, prioritizedTasks.getFirst(), "Список по времени выполнения не отсортирован.");
    }
}