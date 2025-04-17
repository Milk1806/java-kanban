package managerTest;

import file.FileBackedTaskManager;
import file.ManagerSaveException;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    FileBackedTaskManager file = new FileBackedTaskManager();

    @Test
    void saveTaskInFileAndReadFromFile() {
        File tempFile;
        String str;
        Task task1 = new Task(file.getNewID(), "1#4", "1#4");
        Task task2 = new Task(file.getNewID(), "0132", "0132");
        try {
            tempFile = File.createTempFile("Temp_file", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Writer writer = new FileWriter(tempFile)) {
            str = file.toString(task1);
            writer.write(str);
            str = file.toString(task2);
            writer.write(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        file = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(file.getTasks().size(), 2);
    }

    @Test
    void addTask() {
        Task task = new Task(file.getNewID(), "1", "1");
        file.addTask(task);
        assertEquals(task, file.getTaskByld(task.getID()), "Задача не добавлена");
    }

    @Test
    void addEpic() {
        Epic epic = new Epic(file.getNewID(), "1", "1");
        file.addEpic(epic);
        assertEquals(epic, file.getEpicByld(epic.getID()), "Задача не добавлена");
    }

    @Test
    void addSubtask() {
        file.addEpic(new Epic(file.getNewID(), "1", "1"));
        Subtask subtask = new Subtask(file.getNewID(), "111", "111", 1);
        file.addSubtask(subtask);
        assertEquals(subtask, file.getSubtaskByld(subtask.getID()), "Задача не добавлена");
    }

    @Test
    void clearTasks() {
        Task task = new Task(file.getNewID(), "1", "1");
        file.addTask(task);
        file.clearTasks();
        assertTrue(file.getTasks().isEmpty(), "Список задач не очистился");
    }

    @Test
    void clearEpics() {
        Epic epic = new Epic(file.getNewID(), "1", "1");
        file.addEpic(epic);
        Subtask subtask = new Subtask(file.getNewID(), "222", "222", 1);
        file.addSubtask(subtask);
        file.clearEpics();
        assertTrue(file.getEpics().isEmpty() && file.getSubtasks().isEmpty(),
                "Списки суперзадач и подзадач не очищены");
    }

    @Test
    void clearSubtasks() {
        Epic epic = new Epic(file.getNewID(), "1", "1");
        file.addEpic(epic);
        Subtask subtask = new Subtask(file.getNewID(), "111", "111", 1);
        file.addSubtask(subtask);
        file.clearSubtasks();
        assertTrue(file.getSubtasks().isEmpty() && epic.getSubtaskList().isEmpty(),
                "Список подзадач не очищен");
    }

    @Test
    void removeTaskOnID() {
        Task task = new Task(file.getNewID(), "1", "1");
        file.addTask(task);
        file.getTaskByld(1);
        file.removeTaskOnID(task.getID());
        assertFalse(file.getTasks().containsKey(task.getID()),
                "Задача не была удалена из списка");
    }

    @Test
    void removeEpicOnID() {
        Epic epic = new Epic(file.getNewID(), "1", "1");
        file.addEpic(epic);
        file.getEpicByld(1);
        file.removeEpicOnID(epic.getID());
        assertFalse(file.getEpics().containsKey(epic.getID()), "Суперзадача не была удалена");
    }

    @Test
    void removeSubtaskOnID() {
        Epic epic = new Epic(file.getNewID(), "1", "1");
        file.addEpic(epic);
        Subtask subtask = new Subtask(file.getNewID(), "111", "111", 1);
        file.addSubtask(subtask);
        file.getEpicByld(1);
        file.getSubtaskByld(2);
        file.removeSubtaskOnID(subtask.getID());
        assertFalse(file.getSubtasks().containsKey(subtask.getID())
                        && epic.getSubtaskList().contains(subtask),
                "Подзадача не была удалена из списка подзадач и списка подзадач суперзадачи");
    }

    @Test
    void updateTask() {
        Task task = new Task(file.getNewID(), "1", "1");
        file.addTask(task);
        file.updateTask(new Task(1, "2", "2"));
        assertNotEquals(task, file.getTaskByld(task.getID()), "Задача не обновилась");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic(file.getNewID(), "1", "1");
        file.addEpic(epic);
        file.updateEpic(new Epic(1, "2", "2"));
        assertNotEquals(epic, file.getEpicByld(epic.getID()), "Суперзадача не обновилась");
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic(file.getNewID(), "1", "1");
        file.addEpic(epic);
        Subtask subtask = new Subtask(file.getNewID(), "111", "111", 1);
        file.addSubtask(subtask);
        file.updateSubtask(new Subtask(2, "2", "2", 1,
                TaskStatus.DONE));
        assertFalse(subtask.equals(file.getSubtaskByld(subtask.getID()))
                && subtask.equals(epic.getSubtaskList().getFirst()), "Подзадача не обновилась");
    }
}