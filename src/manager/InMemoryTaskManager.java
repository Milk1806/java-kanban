package manager;

import task.*;

import java.util.ArrayList;
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
        if (epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getID(), subtask);
            epics.get(subtask.getEpicId()).addSubtask(subtask);
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
        List<Subtask> taskListOfEpic = new ArrayList<>();
        if (epics.containsKey(epicID)) {
            taskListOfEpic = epics.get(epicID).getSubtaskList();
        }
        return taskListOfEpic;
    }

    @Override
    public void clearTasks() {
        List<Task> list = historyManager.getHistory();
        for (Task task : list) {
            if (tasks.containsKey(task.getID())) {
                historyManager.remove(task.getID());
            }
        }
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        List<Task> list = historyManager.getHistory();
        for (Task task : list) {
            if (epics.containsKey(task.getID()) || subtasks.containsKey(task.getID())) {
                historyManager.remove(task.getID());
            }
        }
        epics.clear();
        clearSubtasks();
    }

    @Override
    public void clearSubtasks() {
        List<Task> list = historyManager.getHistory();
        for (Task task : list) {
            if (subtasks.containsKey(task.getID())) {
                historyManager.remove(task.getID());
            }
        }
        for (Epic epic : epics.values()) {
            epic.getSubtaskList().clear();
        }
        subtasks.clear();
    }

    @Override
    public Task getTaskByld(int taskID) {
        if (tasks.containsKey(taskID)) {
            historyManager.add(tasks.get(taskID));
        }
        return tasks.getOrDefault(taskID, null);
    }

    @Override
    public Epic getEpicByld(int epicID) {
        if (epics.containsKey(epicID)) {
            historyManager.add(epics.get(epicID));
        }
        return epics.getOrDefault(epicID, null);
    }

    @Override
    public Subtask getSubtaskByld(int subtaskID) {
        if (subtasks.containsKey(subtaskID)) {
            historyManager.add(subtasks.get(subtaskID));
        }
        return subtasks.getOrDefault(subtaskID, null);
    }

    @Override
    public void removeTaskOnID(int taskID) {
        if (tasks.containsKey(taskID)) {
            tasks.remove(taskID);
            historyManager.remove(taskID);
        }
    }

    @Override
    public void removeEpicOnID(int epicID) {
        if (epics.containsKey(epicID)) {
            Epic epic = epics.get(epicID);
            for (Subtask subtask : epic.getSubtaskList()) {
                historyManager.remove(subtask.getID());
                subtasks.remove(subtask.getID());
            }
            epics.remove(epicID);
            historyManager.remove(epicID);
        }
    }

    @Override
    public void removeSubtaskOnID(int subtaskID) {
        if (subtasks.containsKey(subtaskID)) {
            Subtask subtask = subtasks.get(subtaskID);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtaskList().remove(subtask);
            subtasks.remove(subtaskID);
            historyManager.remove(subtaskID);
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
                epic.addSubtask(subtask);
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
            Epic epic = epics.get(subtask.getEpicId());
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




