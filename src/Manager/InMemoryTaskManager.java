package Manager;
import Tasks.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    Map<Integer, Task> tasks;
    Map<Integer, Epic> epics;
    Map<Integer, Subtask> subtasks;
    HistoryManager historyManager;
    private int id;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public int getNewID() {
        return ++id;
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getID(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getID(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicID())) {
            subtasks.put(subtask.getID(), subtask);
            epics.get(subtask.getEpicID()).setSubtaskList(subtask);
        }
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int epicID) {
        if (epics.containsKey(epicID)) {
            return epics.get(epicID).getSubtaskList();
        } else {
            return null;
        }
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        clearSubtasks();
    }

    @Override
    public void clearSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskList().clear();
        }
        subtasks.clear();
    }

    @Override
    public Task getTaskOnID(int taskID) {
        historyManager.add(tasks.get(taskID));
        return tasks.getOrDefault(taskID, null);
    }

    @Override
    public Epic getEpicOnID(int epicID) {
        historyManager.add(epics.get(epicID));
        return epics.getOrDefault(epicID, null);
    }

    @Override
    public Subtask getSubtaskOnID(int subtaskID) {
        historyManager.add(subtasks.get(subtaskID));
       return subtasks.getOrDefault(subtaskID, null);
    }

    @Override
    public void removeTaskOnID(int taskID) {
       tasks.remove(taskID);
    }

    @Override
    public void removeEpicOnID(int epicID) {
        if (epics.containsKey(epicID)) {
            Epic epic = epics.get(epicID);
            for (Subtask subtask : epic.getSubtaskList()) {
                subtasks.remove(subtask.getID());
            }
        }
        epics.remove(epicID);
    }

    @Override
    public void removeSubtaskOnID(int subtaskID) {
        if (subtasks.containsKey(subtaskID)) {
            Subtask subtask = subtasks.get(subtaskID);
            Epic epic = epics.get(subtask.getEpicID());
            epic.getSubtaskList().remove(subtask);
            subtasks.remove(subtaskID);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getID())) {
            tasks.put(task.getID(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getID())) {
            Epic oldEpic = epics.get(epic.getID());
            List<Subtask> oldList = oldEpic.getSubtaskList();
            for (Subtask subtask : oldList) {
                epic.setSubtaskList(subtask);
            }

            List<Subtask> newList = epic.getSubtaskList();
            TaskStatus newStatus;

            int amountOfNew = 0;
            int amountOfIsDone = 0;

            for (Subtask subtask : newList) {
                TaskStatus status = subtask.getStatus();

                if (status == TaskStatus.NEW) {
                    amountOfNew++;
                } else if (status == TaskStatus.DONE) {
                    amountOfIsDone++;
                }
            }

            if (amountOfNew == newList.size() || newList.isEmpty()) {
                newStatus = TaskStatus.NEW;
            } else if (amountOfIsDone == newList.size()) {
                newStatus = TaskStatus.DONE;
            } else {
                newStatus = TaskStatus.IN_PROGRESS;
            }
            epic.setStatus(newStatus);
            epics.put(epic.getID(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getID())) {
            Epic epic = epics.get(subtask.getEpicID());
            List<Subtask> list = epic.getSubtaskList();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getID() == subtask.getID()) {
                    list.add(i, subtask);
                    list.remove(i + 1);
                    break;
                }
            }
            subtasks.put(subtask.getID(), subtask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}




