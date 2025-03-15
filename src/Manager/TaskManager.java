package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.util.List;
import java.util.Map;

public interface TaskManager {
    int getNewID();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    Map<Integer, Task> getTasks();

    Map<Integer, Epic> getEpics();

    Map<Integer, Subtask> getSubtasks();

    List<Subtask> getSubtasksOfEpic(int epicID);

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    Task getTaskOnID(int taskID);

    Epic getEpicOnID(int epicID);

    Subtask getSubtaskOnID(int subtaskID);

    void removeTaskOnID(int taskID);

    void removeEpicOnID(int epicID);

    void removeSubtaskOnID(int subtaskID);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    List<Task> getHistory();
}
