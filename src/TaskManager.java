import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;
    private int ID = 0;

    TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    protected int getID() {
        return ++ID;
    }

    public void addTask(Task task) {
        tasks.put(task.getID(), task);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getID(), epic);
    }

    public void addSubtask(Subtask subtask) {
            subtasks.put(subtask.getID(), subtask);
            epics.get(subtask.getEpicID()).setSubtaskList(subtask);
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public ArrayList<Subtask> getSubtasksOfEpic(int epicID) {
        if (epics.containsKey(epicID)) {
            return epics.get(epicID).getSubtaskList();
        } else {
            return null;
        }
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
        clearSubtasks();
    }

    public void clearSubtasks () {
       subtasks.clear();
    }

    public Task getTaskOnID (int taskID) {
        return tasks.getOrDefault(taskID, null);
    }

    public Epic getEpicOnID(int epicID) {
        return epics.getOrDefault(epicID, null);
    }

    public Subtask getSubtaskOnID(int subtaskID) {
       return subtasks.getOrDefault(subtaskID, null);
    }

    public void removeTaskOnID(int taskID) {
       tasks.remove(taskID);
    }

    public void removeEpicOnID(int epicID) {
        if (epics.containsKey(epicID)) {
            Epic epic = epics.get(epicID);
            for (Subtask subtask : epic.getSubtaskList()) {
                subtasks.remove(subtask.getID());
            }
            epics.remove(epicID);
        }
    }

    public void removeSubtaskOnID(int subtaskID) {
        if (subtasks.containsKey(subtaskID)) {
            Subtask subtask = subtasks.get(subtaskID);
            Epic epic = epics.get(subtask.getEpicID());
            epic.getSubtaskList().remove(subtask);
            subtasks.remove(subtaskID);
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getID())) {
            tasks.put(task.getID(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getID())) {
            Epic oldEpic = epics.get(epic.getID());
            ArrayList<Subtask> oldList = oldEpic.getSubtaskList();
            for (Subtask subtask : oldList) {
                epic.setSubtaskList(subtask);
            }

            ArrayList<Subtask> newList = epic.getSubtaskList();
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

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getID())) {
            for (Epic value : epics.values()) {
                ArrayList<Subtask> list = value.getSubtaskList();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getID() == subtask.getID()) {
                        list.remove(i);
                        list.add(i, subtask);
                        break;
                    }
                }
            }
            subtasks.put(subtask.getID(), subtask);
        }
    }
}


