package server;

import com.google.gson.Gson;
import file.Managers;
import file.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EpicsHandlerTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public EpicsHandlerTest() throws IOException {
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
    public void getEpics() throws IOException, InterruptedException {
        manager.addEpic(new Epic(manager.getNewID(), "1","1"));
        manager.addEpic(new Epic(manager.getNewID(), "2","2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Map<Integer, Epic> tasksFromManager = gson.fromJson(response.body(), new TypeTokenEpicsMap().getType());
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void getEpicOnId() throws IOException, InterruptedException {
        Epic epic = new Epic(manager.getNewID(), "1","1");
        manager.addEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic responseEpic = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic, responseEpic, "Задачи не совпадают.");
    }

    @Test
    public void getSubtasksListOnEpicId() throws IOException, InterruptedException {
        Epic epic = new Epic(manager.getNewID(), "1","1");
        Subtask subtask1 = new Subtask(manager.getNewID(), "!!!", "!!!", 1, TaskStatus.NEW, "2025-05-01T12:00", 60);
        Subtask subtask2 = new Subtask(manager.getNewID(), "^^^", "^^^", 1, TaskStatus.NEW, "2025-05-01T14:00", 60);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List <Task> subtasksList = gson.fromJson(response.body(), new TypeTokenTasksList().getType());
        assertEquals(2, subtasksList.size(), "Некорректное количество задач");
    }

    @Test
    public void deleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(manager.getNewID(), "1","1");
        manager.addEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(manager.getEpicByld(epic.getID()).isEmpty());
    }
}