package TasksTest;

import Manager.Managers;
import Manager.TaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    TaskManager manager = Managers.getDefault();

    @Test
    void ifEpicIdEqualAnotherEpicIdEpicsIsEqual() {
        Epic epic1 = new Epic(manager.getNewID(), "1", "1");
        Epic epic2 = epic1;
        assertEquals(epic1, epic2, "Суперзадачи не равны");
    }
}