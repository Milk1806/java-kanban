package file;

import task.Epic;
import task.Subtask;
import task.Task;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TaskManager {
    int getNewID();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    Map<Integer, Task> getTasks();

    Map<Integer, Epic> getEpics();

    Map<Integer, Subtask> getSubtasks();

    Optional<List<Subtask>> getSubtasksOfEpic(int epicID);

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    Optional<Task> getTaskByld(int taskID);

    Optional<Epic> getEpicByld(int epicID);

    Optional<Subtask> getSubtaskByld(int subtaskID);

    void removeTaskOnID(int taskID);

    void removeEpicOnID(int epicID);

    void removeSubtaskOnID(int subtaskID);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    List<Task> getHistory();

    public List<Task> getPrioritizedTasks();

    public void updateEpicTime(Epic epic);

    public boolean intersectionOfTasks(Task task);
}
