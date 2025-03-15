package ManagerTest;

import Manager.Managers;
import Manager.TaskManager;
import Tasks.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager manager = Managers.getDefault();

    @Test
    void getNewID() {
        Task task1 = new Task(manager.getNewID(), "1", "1");
        Task task2 = new Task(manager.getNewID(), "2", "2");
        assertNotEquals(task1, task2, "ID равны");
    }

    @Test
    void addTask() {
        Task task = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task);
        assertEquals(task, manager.getTaskOnID(task.getID()), "Задача не добавлена");
    }

    @Test
    void addEpic() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        assertEquals(epic, manager.getEpicOnID(epic.getID()), "Задача не добавлена");
    }

    @Test
    void addSubtask() {
        manager.addEpic(new Epic(manager.getNewID(), "1", "1"));
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1);
        manager.addSubtask(subtask);
        assertEquals(subtask, manager.getSubtaskOnID(subtask.getID()), "Задача не добавлена");
    }

    @Test
    void getTasks() {
        Task task = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task);
        Map<Integer, Task> newTasks = manager.getTasks();
        assertEquals(task, newTasks.get(task.getID()), "Невозможно получить задачи из списка задач");
    }

    @Test
    void getEpics() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        Map<Integer, Epic> newEpics = manager.getEpics();
        assertEquals(epic, newEpics.get(epic.getID()), "Невозможно получить супер задачи из списка"
                + " супер задач");
    }

    @Test
    void getSubtasks() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1);
        manager.addSubtask(subtask);
        Map<Integer, Subtask> newSubtask = manager.getSubtasks();
        assertEquals(subtask, newSubtask.get(subtask.getID()), "Невозможно получить подзадачи"
                + " из списка подзадач");
    }

    @Test
    void getSubtasksOfEpic() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask(manager.getNewID(), "111", "111", 1);
        Subtask subtask2 = new Subtask(manager.getNewID(), "222", "222", 1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        Subtask[] expectedSubtasksList = {subtask1, subtask2};
        Subtask[] actualSubtaskList = new Subtask[2];
        List<Subtask> subtaskList = manager.getSubtasksOfEpic(epic.getID());
        for (int i = 0; i < subtaskList.size(); i++) {
            Subtask subtask = subtaskList.get(i);
            actualSubtaskList[i] = subtask;
        }
        assertArrayEquals(expectedSubtasksList, actualSubtaskList, "Невозможно получить"
                + " список подзадач суперзадачи");
   }

    @Test
    void clearTasks() {
        Task task = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task);
        manager.clearTasks();
        assertTrue(manager.getTasks().isEmpty(), "Список задач не очистился");
    }

    @Test
    void clearEpics() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask(manager.getNewID(), "222", "222", 1);
        manager.addSubtask(subtask);
        manager.clearEpics();
        assertTrue(manager.getEpics().isEmpty() && manager.getSubtasks().isEmpty(),
                "Списки суперзадач и подзадач не очищены");
    }

    @Test
    void clearSubtasks() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1);
        manager.addSubtask(subtask);
        manager.clearSubtasks();
        assertTrue(manager.getSubtasks().isEmpty() && epic.getSubtaskList().isEmpty(),
                "Список подзадач не очищен");
    }

    @Test
    void getTaskOnID() {
        Task task = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task);
        assertEquals(task, manager.getTaskOnID(task.getID()),
                "Задачи с необходимым id нет в списке");
    }

    @Test
    void getEpicOnID() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        assertEquals(epic, manager.getEpicOnID(epic.getID()),
                "Суперзадачи с необходимым id нет в списке");
    }

    @Test
    void getSubtaskOnID() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1);
        manager.addSubtask(subtask);
        assertEquals(subtask, manager.getSubtaskOnID(subtask.getID()),
                "Подзадачи с необходимым id нет в списке");
    }

    @Test
    void removeTaskOnID() {
        Task task = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task);
        manager.removeTaskOnID(task.getID());
        assertFalse(manager.getTasks().containsKey(task.getID()),
                "Задача не была удалена из списка");
    }

    @Test
    void removeEpicOnID() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        manager.removeEpicOnID(epic.getID());
        assertFalse(manager.getEpics().containsKey(epic.getID()), "Суперзадача не была удалена");
    }

    @Test
    void removeSubtaskOnID() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1);
        manager.addSubtask(subtask);
        manager.removeSubtaskOnID(subtask.getID());
        assertFalse(manager.getSubtasks().containsKey(subtask.getID())
                && epic.getSubtaskList().contains(subtask),
                "Подзадача не была удалена из списка подзадач и списка подзадач суперзадачи");
    }

    @Test
    void updateTask() {
        Task task = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task);
        manager.updateTask(new Task(1, "2", "2"));
        assertNotEquals(task, manager.getTaskOnID(task.getID()), "Задача не обновилась");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        manager.updateEpic(new Epic(1, "2", "2"));
        assertNotEquals(epic, manager.getEpicOnID(epic.getID()), "Суперзадача не обновилась");
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1);
        manager.addSubtask(subtask);
        manager.updateSubtask(new Subtask(2, "2", "2", 1,
                TaskStatus.DONE));
        assertFalse(subtask.equals(manager.getSubtaskOnID(subtask.getID()))
                && subtask.equals(epic.getSubtaskList().getFirst()), "Подзадача не обновилась");
    }

    @Test
    void getHistory() {
        Task task = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task);
        manager.getTaskOnID(task.getID());
        List <Task> expectedList = new ArrayList<>();
        expectedList.add(task);
        List <Task> actualeList = manager.getHistory();
        assertEquals(expectedList, actualeList, "Задача не добавлена в историю просмотров");
    }

    @Test
    void newTaskEqualAddedTask() {
        Task task = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task);
        assertEquals(task, manager.getTaskOnID(task.getID()));
    }

    @Test
    void ifChangeStatusInSubtaskStatusInEpicChangeTo() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1);
        manager.addSubtask(subtask);
        manager.updateSubtask(new Subtask(2, "!!!", "!!!", 1,
                TaskStatus.DONE));
        manager.updateEpic(new Epic(1, "&&&", "&&&"));
        assertEquals(TaskStatus.DONE, manager.getEpics().get(epic.getID()).getStatus());
    }
}