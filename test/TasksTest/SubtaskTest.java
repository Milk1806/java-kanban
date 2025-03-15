package TasksTest;

import Manager.Managers;
import Manager.TaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import org.junit.jupiter.api.Test;

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
}