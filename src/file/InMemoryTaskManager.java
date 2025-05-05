package file;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    Map<Integer, Task> tasks;
    Map<Integer, Epic> epics;
    Map<Integer, Subtask> subtasks;
    HistoryManager historyManager;
    private int id;
    TreeSet<Task> sortedTasks;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        sortedTasks = new TreeSet<>(Comparator.comparing(task -> task.getStartTime().get()));
    }

    @Override
    public int getNewID() {
        return ++id;
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getID(), task);
        if (task.getStartTime().isPresent()) {
            if (sortedTasks.isEmpty() || intersectionOfTasks(task)) {
                sortedTasks.add(task);
            }
        }
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
            if (subtask.getStartTime().isPresent()) {
                if (sortedTasks.isEmpty() || intersectionOfTasks(subtask)) {
                    sortedTasks.add(subtask);
                    updateEpicTime(epics.get(subtask.getEpicId()));
                }
            }
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
        list.stream()
                .filter(task -> tasks.containsKey(task.getID()))
                .forEach(task -> historyManager.remove(task.getID()));
        sortedTasks.removeIf(task -> task instanceof Task);
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        List<Task> list = historyManager.getHistory();
        list.stream()
                .filter(task -> epics.containsKey(task.getID()) || subtasks.containsKey(task.getID()))
                .forEach(task -> historyManager.remove(task.getID()));
        epics.clear();
        sortedTasks.removeIf(subtask -> subtask instanceof Subtask);
        clearSubtasks();
    }

    @Override
    public void clearSubtasks() {
        List<Task> list = historyManager.getHistory();
        list.stream()
                .filter(task -> subtasks.containsKey(task.getID()))
                .forEach(task -> historyManager.remove(task.getID()));
        for (Epic epic : epics.values()) {
            epic.getSubtaskList().clear();
            updateEpicTime(epic);
        }
        sortedTasks.removeIf(subtask -> subtask instanceof Subtask);
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
            if (historyManager.getHistory().contains(tasks.get(taskID))) {
                historyManager.remove(taskID);
            }
            sortedTasks.remove(tasks.get(taskID));
            tasks.remove(taskID);
        }
    }

    @Override
    public void removeEpicOnID(int epicID) {
        if (epics.containsKey(epicID)) {
            Epic epic = epics.get(epicID);
            for (Subtask subtask : epic.getSubtaskList()) {
                if (historyManager.getHistory().contains(subtasks.get(epicID))) {
                    historyManager.remove(subtask.getID());
                }
                if (sortedTasks.contains(subtask)) {
                    sortedTasks.remove(subtask);
                }
                subtasks.remove(subtask.getID());

            }
            if (historyManager.getHistory().contains(epics.get(epicID))) {
                historyManager.remove(epicID);
            }
            epics.remove(epicID);
        }
    }

    @Override
    public void removeSubtaskOnID(int subtaskID) {
        if (subtasks.containsKey(subtaskID)) {
            if (historyManager.getHistory().contains(subtasks.get(subtaskID))) {
                historyManager.remove(subtaskID);
            }
            Subtask subtask = subtasks.get(subtaskID);
            if (sortedTasks.contains(subtask)) {
                sortedTasks.remove(subtask);
            }
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtaskList().remove(subtask);
            subtasks.remove(subtaskID);
            updateEpicTime(epic);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getID())) {
            sortedTasks.remove(tasks.get(task.getID()));
            tasks.put(task.getID(), task);
            if (task.getStartTime().isPresent()) {
                if (sortedTasks.isEmpty() || intersectionOfTasks(task)) {
                    sortedTasks.add(task);
                }
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getID())) {
            Epic oldEpic = epics.get(epic.getID());
            List<Subtask> oldList = oldEpic.getSubtaskList();
            oldList.stream().forEach(epic::addSubtask);
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
            sortedTasks.remove(subtasks.get(subtask.getID()));
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
            if (subtask.getStartTime().isPresent()) {
                if (sortedTasks.isEmpty() || intersectionOfTasks(subtask)) {
                    sortedTasks.add(subtask);
                }
            }
            updateEpicTime(epic);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void updateEpicTime(Epic epic) {
        List<Subtask> subtaskList = epic.getSubtaskList();

        epic.setStartTime(
            subtaskList.stream()
                    .map(Subtask::getStartTime)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .min(LocalDateTime::compareTo)
                    .orElseGet(() -> {
                        return null;
                    })
        );
        epic.setDuration(
            subtaskList.stream()
                    .map(Subtask::getDuration)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .reduce(Duration::plus)
                    .orElseGet(() -> {
                        return Duration.ofSeconds(0);
                    })
        );
        epic.setEndTime(
            subtaskList.stream()
                    .filter(s -> s.getStartTime().isPresent() && s.getDuration().isPresent())
                    .map(Subtask::getEndTime)
                    .map(Optional::get)
                    .max(LocalDateTime::compareTo)
                    .orElseGet(() -> {
                        return null;
                    })
        );
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks);
    }

    public boolean intersectionOfTasks(Task task) {
        List<Task> list = getPrioritizedTasks();
        return list.stream()
                .allMatch(listTask -> {
                    LocalDateTime startTime1 = task.getStartTime().get();
                    LocalDateTime endTime1 = task.getEndTime().get();
                    LocalDateTime startTime2 = listTask.getStartTime().get();
                    LocalDateTime endTime2 = listTask.getEndTime().get();
                    return (endTime1.isBefore(startTime2) || (startTime1.isAfter(endTime2)));
                });
    }
}




