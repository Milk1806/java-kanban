package managerTest;

import file.Managers;
import file.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
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
        manager.getTaskByld(task1.getID());
        manager.getTaskByld(task2.getID());
        assertEquals(task1, manager.getHistory().getFirst());
    }

    @Test
    void taskInHistoryDeletedIfAddNewTaskWithTheSameId() {
        Task task1 = new Task(manager.getNewID(), "1", "1");
        Epic epic = new Epic(manager.getNewID(), "2", "2");
        Subtask subtask = new Subtask(manager.getNewID(), "222", "222", 2);
        manager.addTask(task1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.getTaskByld(1);
        manager.getEpicByld(2);
        manager.getSubtaskByld(3);
        manager.getTaskByld(1);
        int i = manager.getHistory().size();
        assertEquals(3, i);
    }

    @Test
    void taskInHistoryWillBeDeletedIfRemoveTaskOnId() {
        Task task1 = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task1);
        manager.getTaskByld(1);
        manager.removeTaskOnID(1);
        int i = manager.getHistory().size();
        assertEquals(0, i);
    }
}