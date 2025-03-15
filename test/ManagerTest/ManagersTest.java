package ManagerTest;

import Manager.HistoryManager;
import Manager.InMemoryTaskManager;
import Manager.Managers;
import Manager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() {
        TaskManager manager = Managers.getDefault();
        assertInstanceOf(TaskManager.class, manager);
    }

    @Test
    void getDefaultHistory() {
        HistoryManager history = Managers.getDefaultHistory();
        assertInstanceOf(HistoryManager.class, history);
    }
}