package taskTest;

import file.Managers;
import file.TaskManager;
import task.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    TaskManager manager = Managers.getDefault();

    @Test
    void ifTaskIdEqualAnotherTaskIdTasksIsEqual() {
        Task task1 = new Task(manager.getNewID(), "1", "1");
        Task task2 = task1;
        assertEquals(task1, task2, "Задачи не равны");
    }

    @Test
    void startTimeIsCorrect() {
        Task task = new Task(manager.getNewID(), "1","1","2025-05-01T12:00",60);
        assertEquals(LocalDateTime.of(2025,05,01,12,0)
                , task.getStartTime().get());
    }

    @Test
    void durationIsCorrect() {
        Task task = new Task(manager.getNewID(), "1","1","2025-05-01T12:00",60);
        assertEquals(Duration.ofMinutes(60), task.getDuration().get());
    }

    @Test
    void endTimeIsCorrect() {
        Task task = new Task(manager.getNewID(), "1","1","2025-05-01T12:00",60);
        assertEquals(LocalDateTime.of(2025,05,01,13,0)
                , task.getEndTime().get());
    }
}