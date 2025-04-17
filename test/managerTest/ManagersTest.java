package managerTest;

import file.HistoryManager;
import file.Managers;
import file.TaskManager;
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