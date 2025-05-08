package managerTest;

import file.TaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    public TaskManagerTest(T manager) {
        this.manager = manager;
    }

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
        assertEquals(task, manager.getTaskByld(task.getID()).get(), "Задача не добавлена");
    }

    @Test
    void addEpic() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        assertEquals(epic, manager.getEpicByld(epic.getID()).get(), "Задача не добавлена");
    }

    @Test
    void addSubtask() {
        manager.addEpic(new Epic(manager.getNewID(), "1", "1"));
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1);
        manager.addSubtask(subtask);
        assertEquals(subtask, manager.getSubtaskByld(subtask.getID()).get(), "Задача не добавлена");
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
        List<Subtask> subtaskList = manager.getSubtasksOfEpic(epic.getID()).get();
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
    void getTaskByld() {
        Task task = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task);
        assertEquals(task, manager.getTaskByld(task.getID()).get(),
                "Задачи с необходимым id нет в списке");
    }

    @Test
    void getSubtaskOnID() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1);
        manager.addSubtask(subtask);
        assertEquals(subtask, manager.getSubtaskByld(subtask.getID()).get(),
                "Подзадачи с необходимым id нет в списке");
    }

    @Test
    void getEpicByld() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        assertEquals(epic, manager.getEpicByld(epic.getID()).get(),
                "Суперзадачи с необходимым id нет в списке");
    }

    @Test
    void removeTaskOnID() {
        Task task = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task);
        manager.getTaskByld(1);
        manager.removeTaskOnID(task.getID());
        assertFalse(manager.getTasks().containsKey(task.getID()),
                "Задача не была удалена из списка");
    }

    @Test
    void removeEpicOnID() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        manager.getEpicByld(1);
        manager.removeEpicOnID(epic.getID());
        assertFalse(manager.getEpics().containsKey(epic.getID()), "Суперзадача не была удалена");
    }

    @Test
    void removeSubtaskOnID() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1);
        manager.addSubtask(subtask);
        manager.getEpicByld(1);
        manager.getSubtaskByld(2);
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
        assertNotEquals(task, manager.getTaskByld(task.getID()), "Задача не обновилась");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        manager.updateEpic(new Epic(1, "2", "2"));
        assertNotEquals(epic, manager.getEpicByld(epic.getID()), "Суперзадача не обновилась");
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        Subtask subtask = new Subtask(manager.getNewID(), "111", "111", 1);
        manager.addSubtask(subtask);
        manager.updateSubtask(new Subtask(2, "2", "2", 1,
                TaskStatus.DONE));
        assertFalse(subtask.equals(manager.getSubtaskByld(subtask.getID()))
                && subtask.equals(epic.getSubtaskList().getFirst()), "Подзадача не обновилась");
    }

    @Test
    void getPrioritizedTasksIsCorrectly() {
        manager.addTask(new Task(manager.getNewID(), "1","1","2025-05-01T12:00"
                , 60));
        manager.addTask(new Task(manager.getNewID(), "2","2","2025-05-02T12:00"
                , 60));
        manager.addEpic(new Epic(manager.getNewID(), "3","3"));
        manager.addSubtask(new Subtask(manager.getNewID(), "3-3-3","3-3-3",3
                , "2025-05-01T10:00",60));
        manager.addSubtask(new Subtask(manager.getNewID(), "33-33-33","33-33-33",3
                , "2025-05-01T10:30",60));
        manager.removeTaskOnID(1);
        List<Task> list = new ArrayList<>(manager.getPrioritizedTasks());
        assertEquals(manager.getSubtaskByld(4).get(), list.getFirst());
    }

    @Test
    void newTaskEqualAddedTask() {
        Task task = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task);
        assertEquals(task, manager.getTaskByld(task.getID()).get());
    }

    @Test
    void ifBothSubtaskHaveStatusNewEpicStatusIsNewTo() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        manager.addSubtask(new Subtask(manager.getNewID(), "111", "111", 1));
        manager.addSubtask(new Subtask(manager.getNewID(), "222", "222", 1));
        manager.updateEpic(new Epic(1, "&&&", "&&&"));
        assertEquals(TaskStatus.NEW, manager.getEpics().get(epic.getID()).getStatus());
    }

    @Test
    void ifBothSubtaskHaveStatusDoneEpicStatusIsDoneTo() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        manager.addSubtask(new Subtask(manager.getNewID(), "111", "111", 1));
        manager.addSubtask(new Subtask(manager.getNewID(), "222", "222", 1));
        manager.updateSubtask(new Subtask(2, "111","111",1,TaskStatus.DONE));
        manager.updateSubtask(new Subtask(3, "222","222",1,TaskStatus.DONE));
        manager.updateEpic(new Epic(1, "&&&", "&&&"));
        assertEquals(TaskStatus.DONE, manager.getEpics().get(epic.getID()).getStatus());
    }

    @Test
    void ifSubtasksHaveStatusDoneAndNewEpicStatusIsInProgress() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        manager.addSubtask(new Subtask(manager.getNewID(), "111", "111", 1));
        manager.addSubtask(new Subtask(manager.getNewID(), "222", "222", 1));
        manager.updateSubtask(new Subtask(2, "111","111",1,TaskStatus.NEW));
        manager.updateSubtask(new Subtask(3, "222","222",1,TaskStatus.DONE));
        manager.updateEpic(new Epic(1, "&&&", "&&&"));
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpics().get(epic.getID()).getStatus());
    }

    @Test
    void ifBothSubtaskHaveStatusInProgressEpicStatusIsInProgressTo() {
        Epic epic = new Epic(manager.getNewID(), "1", "1");
        manager.addEpic(epic);
        manager.addSubtask(new Subtask(manager.getNewID(), "111", "111", 1));
        manager.addSubtask(new Subtask(manager.getNewID(), "222", "222", 1));
        manager.updateSubtask(new Subtask(2, "111","111",1,TaskStatus.IN_PROGRESS));
        manager.updateSubtask(new Subtask(3, "222","222",1,TaskStatus.IN_PROGRESS));
        manager.updateEpic(new Epic(1, "&&&", "&&&"));
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpics().get(epic.getID()).getStatus());
    }

    @Test
    void getHistory() {
        Task task = new Task(manager.getNewID(), "1", "1");
        manager.addTask(task);
        manager.getTaskByld(task.getID());
        List <Task> expectedList = new ArrayList<>();
        expectedList.add(task);
        List <Task> actualeList = manager.getHistory();
        assertEquals(expectedList, actualeList, "Задача не добавлена в историю просмотров");
    }

    @Test
    void updateEpicTime() {
        Epic epic = new Epic(manager.getNewID(), "1","1");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask(manager.getNewID(), "!","!",1,
                "2025-05-01T21:00",60);
        Subtask subtask2 = new Subtask(manager.getNewID(), "@","@",1,
                "2025-05-01T23:00",60);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        assertEquals(Duration.ofMinutes(120), epic.getDuration().get());
    }

    @Test
    void intersectionOfTasks() {
        Task task1 = new Task(manager.getNewID(), "1","1",
                "2025-05-01T21:00",60);
        Task task2 = new Task(manager.getNewID(), "1","1",
                "2025-05-01T21:00",60);
        manager.addTask(task1);
        manager.addTask(task2);
        assertEquals(task1, manager.getPrioritizedTasks().getFirst());
        assertEquals(1, manager.getPrioritizedTasks().size());
    }


}