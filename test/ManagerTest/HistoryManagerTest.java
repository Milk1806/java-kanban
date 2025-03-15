package ManagerTest;

import Manager.Managers;
import Manager.TaskManager;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    TaskManager manager = Managers.getDefault();

    @Test
    void taskInHistoryNotDeletedIfAddNewTask() {
        Task task1 = new Task(manager.getNewID(), "1", "1");
        Task task2 = new Task(manager.getNewID(), "2", "2");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.getTaskOnID(task1.getID());
        manager.getTaskOnID(task2.getID());
        assertEquals(task1, manager.getHistory().getFirst());
    }

    @Test
    void ifAddTaskInHistoryMoreThan10FirstTaskIsDeleted() {
        Task task1 = new Task(manager.getNewID(), "1", "1");
        Task task2 = new Task(manager.getNewID(), "2", "2");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.getTaskOnID(task2.getID());
        for (int i = 0; i < 10; i++) {
            manager.getTaskOnID(task1.getID());
        }
        assertNotEquals(task2, manager.getHistory().getFirst());
    }

    @Test
    void taskWillBeAddedInLastPlaceInHistoryIfTaskMoreThan10() {
        Task task1 = new Task(manager.getNewID(), "1", "1");
        Task task2 = new Task(manager.getNewID(), "2", "2");
        manager.addTask(task1);
        manager.addTask(task2);
        for (int i = 0; i < 10; i++) {
            manager.getTaskOnID(task1.getID());
        }
        manager.getTaskOnID(task2.getID());
        assertEquals(task2, manager.getHistory().getLast());
    }
}