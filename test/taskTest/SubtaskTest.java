package taskTest;

import file.Managers;
import file.TaskManager;
import task.Epic;
import task.Subtask;
import org.junit.jupiter.api.Test;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    TaskManager manager = Managers.getDefault();

    @Test
    void ifSubtaskIdEqualAnotherSubtaskIdSubtasksIsEqual() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        Subtask subtask1 = new Subtask(manager.getNewID(), "111", "111", 1);
        Subtask subtask2 = subtask1;
        assertEquals(subtask1, subtask2, "Подзадачи не равны");
    }

    @Test
    void startTimeIsCorrect() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1
                ,"2025-05-01T12:00",60);
        assertEquals(LocalDateTime.of(2025,05,01,12,0)
                , subtask.getStartTime().get());
    }

    @Test
    void durationIsCorrect() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1
                ,"2025-05-01T12:00",60);
        assertEquals(Duration.ofMinutes(60), subtask.getDuration().get());
    }

    @Test
    void endTimeIsCorrect() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1
                ,"2025-05-01T12:00",60);
        assertEquals(LocalDateTime.of(2025,05,01,13,0)
                , subtask.getEndTime().get());
    }

    @Test
    void subtaskIsInEpicSubtasksList() {
        Epic epic = new Epic(manager.getNewID(), "1","1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask(manager.getNewID(), "111","111",1);
        manager.addSubtask(subtask);
        assertEquals(subtask, epic.getSubtaskList().getFirst());

    }
}