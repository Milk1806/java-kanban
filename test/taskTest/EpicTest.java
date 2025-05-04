package taskTest;

import file.Managers;
import file.TaskManager;
import task.Epic;
import org.junit.jupiter.api.Test;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    TaskManager manager = Managers.getDefault();

    @Test
    void ifEpicIdEqualAnotherEpicIdEpicsIsEqual() {
        Epic epic1 = new Epic(manager.getNewID(), "1", "1");
        Epic epic2 = epic1;
        assertEquals(epic1, epic2, "Суперзадачи не равны");
    }

    @Test
    void startTimeIsCorrect() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        manager.addSubtask(new Subtask(manager.getNewID(), "111", "111", 1
                ,"2025-05-01T12:00",60));
        assertEquals(LocalDateTime.of(2025,05,01,12,0)
                , epic.getStartTime().get());
    }

    @Test
    void durationIsCorrect() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        manager.addSubtask(new Subtask(manager.getNewID(), "111", "111", 1
                ,"2025-05-01T12:00",60));
        assertEquals(Duration.ofMinutes(60), epic.getDuration().get());
    }

    @Test
    void endTimeIsCorrect() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        manager.addSubtask(new Subtask(manager.getNewID(), "111", "111", 1
                ,"2025-05-01T12:00",60));
        assertEquals(LocalDateTime.of(2025,05,01,13,0)
                , epic.getEndTime().get());
    }
}