package TasksTest;

import Manager.Managers;
import Manager.TaskManager;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    TaskManager manager = Managers.getDefault();

    @Test
    void ifTaskIdEqualAnotherTaskIdTasksIsEqual() {
        Task task1 = new Task(manager.getNewID(), "1", "1");
        Task task2 = task1;
        assertEquals(task1, task2, "Задачи не равны");
    }


}