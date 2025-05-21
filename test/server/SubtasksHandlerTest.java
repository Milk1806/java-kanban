package server;

import com.google.gson.Gson;
import file.Managers;
import file.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class SubtasksHandlerTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public SubtasksHandlerTest() throws IOException {
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
    public void getSubtasks() throws IOException, InterruptedException {
        manager.addEpic(new Epic(manager.getNewID(), "1","1"));
        manager.addSubtask(new Subtask(manager.getNewID(), "!!!", "!!!", 1, TaskStatus.NEW, "2025-05-01T12:00", 60));
        manager.addSubtask(new Subtask(manager.getNewID(), "@@@", "@@@", 1, TaskStatus.NEW, "2025-05-01T14:00", 60));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Map<Integer, Subtask> subtasksFromManager = gson.fromJson(response.body(), new TypeTokenSubtasksMap().getType());
        assertEquals(2, subtasksFromManager.size(), "Некорректное количество подзадач");
    }

    @Test
    public void getSubtaskOnId() throws IOException, InterruptedException {
        Epic epic = new Epic(manager.getNewID(), "1","1");
        Subtask subtask = new Subtask(manager.getNewID(), "!!!", "!!!", 1, TaskStatus.NEW, "2025-05-01T12:00", 60);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask responseTask = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask, responseTask, "Подзадачи не совпадают.");
    }

    @Test
    public void addSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(manager.getNewID(), "1","1");
        Subtask subtask = new Subtask(manager.getNewID(), "!!!", "!!!", 1, TaskStatus.NEW, "2025-05-01T12:00", 60);
        manager.addEpic(epic);
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Map<Integer, Subtask> subtasksFromManager = manager.getSubtasks();
        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("!!!", subtasksFromManager.get(subtask.getID()).getName(), "Некорректное имя задачи");
    }

    @Test
    public void updateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(manager.getNewID(), "1","1");
        Subtask subtask = new Subtask(manager.getNewID(), "!!!", "!!!", 1, TaskStatus.NEW, "2025-05-01T12:00", 60);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        String subtaskJson = gson.toJson(subtask);
        String newSubtaskJson = subtaskJson.replace("!!!", "___");
        newSubtaskJson = newSubtaskJson.replace("01-05-2025:12.00", "01-05-2025:20.00");
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(newSubtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals("___", manager.getSubtaskByld(2).get().getName(), "Имена не совпадают.");
    }

    @Test
    public void deleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(manager.getNewID(), "1","1");
        Subtask subtask = new Subtask(manager.getNewID(), "!!!", "!!!", 1, TaskStatus.NEW, "2025-05-01T12:00", 60);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(manager.getTaskByld(subtask.getID()).isEmpty());
    }
}