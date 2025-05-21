package server;

import com.google.gson.Gson;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TasksHandlerTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public TasksHandlerTest() throws IOException {
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
    public void getTasks() throws IOException, InterruptedException {
        manager.addTask(new Task(manager.getNewID(), "1", "1", TaskStatus.NEW, "2025-05-01T12:00", 60));
        manager.addTask(new Task(manager.getNewID(), "2", "2", TaskStatus.NEW, "2025-05-01T14:00", 60));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Map<Integer, Task> tasksFromManager = gson.fromJson(response.body(), new TypeTokenTasksMap().getType());
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void getTaskOnId() throws IOException, InterruptedException {
        Task task1 = new Task(manager.getNewID(), "1", "1", TaskStatus.NEW, "2025-05-01T12:00", 60);
        manager.addTask(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task responseTask = gson.fromJson(response.body(), Task.class);
        assertEquals(task1, responseTask, "Задачи не совпадают.");
    }

    @Test
    public void addTask() throws IOException, InterruptedException {
        Task task = new Task(manager.getNewID(), "111", "1", TaskStatus.NEW, "2025-05-01T12:00", 60);
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Map<Integer, Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("111", tasksFromManager.get(task.getID()).getName(), "Некорректное имя задачи");
    }

    @Test
    public void updateTask() throws IOException, InterruptedException {
        Task task = new Task(manager.getNewID(), "123", "1", TaskStatus.NEW, "2025-05-01T12:00", 60);
        manager.addTask(task);
        String taskJson = gson.toJson(task);
        String newTaskJson = taskJson.replace("123", "!!!");
        newTaskJson = newTaskJson.replace("01-05-2025:12.00", "01-05-2025:20.00");
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(newTaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals("!!!", manager.getTaskByld(1).get().getName(), "Имена не совпадают.");
    }

    @Test
    public void deleteTask() throws IOException, InterruptedException {
        Task task = new Task(manager.getNewID(), "123", "1", TaskStatus.NEW, "2025-05-01T12:00", 60);
        manager.addTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(manager.getTaskByld(task.getID()).isEmpty());
    }
}