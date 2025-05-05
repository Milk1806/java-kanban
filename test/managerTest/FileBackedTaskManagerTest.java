package managerTest;

import file.FileBackedTaskManager;
import file.InMemoryTaskManager;
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
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    FileBackedTaskManager file = new FileBackedTaskManager();

    public FileBackedTaskManagerTest() {
        super(new FileBackedTaskManager());
    }

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
        assertEquals(2, file.getTasks().size());
    }

    @Test
    void CorrectlyCatchExceptions() {
        assertThrows(NullPointerException.class, () -> {
            manager.addTask(null);
        }, "Ожидается NullPointerException при ошибке записи в файл");
    }
}