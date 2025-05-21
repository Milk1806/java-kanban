package file;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    Map<Integer, Task> tasks;
    Map<Integer, Epic> epics;
    Map<Integer, Subtask> subtasks;
    HistoryManager historyManager;
    private int id;
    TreeSet<Task> sortedTasks;
    List<Integer> idList;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        sortedTasks = new TreeSet<>(Comparator.comparing(task -> task.getStartTime().get()));
        idList = new ArrayList<>();
        id = 0;
    }

    @Override
    public int getNewID() {
        return ++id;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    @Override
    public void addTask(Task task) {
        boolean isValid = task.getStartTime().isEmpty() ||
                (task.getStartTime().isPresent() && intersectionOfTasks(task));
        if (!(idList.contains(task.getID())) && isValid) {
            tasks.put(task.getID(), task);
            idList.add(task.getID());
            if (task.getStartTime().isPresent()) {
                sortedTasks.add(task);
            }
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (!(idList.contains(epic.getID()))) {
            epics.put(epic.getID(), epic);
            idList.add(epic.getID());
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            boolean isValid = subtask.getStartTime().isEmpty() ||
                    (subtask.getStartTime().isPresent() && intersectionOfTasks(subtask));
            if (!(idList.contains(subtask.getID())) && isValid) {
                subtasks.put(subtask.getID(), subtask);
                idList.add(subtask.getID());
                epics.get(subtask.getEpicId()).addSubtask(subtask);
                if (subtask.getStartTime().isPresent()) {
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
    public Optional<List<Subtask>> getSubtasksOfEpic(int epicID) {
        return epics.containsKey(epicID) ? Optional.of(epics.get(epicID).getSubtaskList()) :
                Optional.empty();
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
    public Optional<Task> getTaskByld(int taskID) {
        if (tasks.containsKey(taskID)) {
            historyManager.add(tasks.get(taskID));
        }
        return Optional.ofNullable(tasks.get(taskID));
    }

    @Override
    public Optional<Epic> getEpicByld(int epicID) {
        if (epics.containsKey(epicID)) {
            historyManager.add(epics.get(epicID));
        }
        return Optional.ofNullable(epics.get(epicID));
    }

    @Override
    public Optional<Subtask> getSubtaskByld(int subtaskID) {
        if (subtasks.containsKey(subtaskID)) {
            historyManager.add(subtasks.get(subtaskID));
        }
        return Optional.ofNullable(subtasks.get(subtaskID));
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
            boolean taskIsPresent = sortedTasks.contains(task);
            Task oldTask = tasks.get(task.getID());
            boolean isValid = task.getStartTime().isEmpty() ||
                    (task.getStartTime().isPresent() && intersectionOfTasks(task));

            tasks.remove(oldTask.getID());
            sortedTasks.remove(oldTask);
            if (isValid) {
                tasks.put(task.getID(), task);
                if (task.getStartTime().isPresent()) {
                    sortedTasks.add(task);
                }
            } else {
                tasks.put(oldTask.getID(), task);
                if (taskIsPresent) {
                    sortedTasks.add(oldTask);
                }
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getID())) {
            Epic oldEpic = epics.get(epic.getID());
            List<Subtask> oldList = oldEpic.getSubtaskList();
            oldList.forEach(epic::addSubtask);
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
            boolean subtaskIsPresent = sortedTasks.contains(subtask);
            Subtask oldSubtask = subtasks.get(subtask.getID());
            Epic epic = epics.get(oldSubtask.getEpicId());
            boolean isValid = subtask.getStartTime().isEmpty() ||
                    (subtask.getStartTime().isPresent() && intersectionOfTasks(subtask));
            sortedTasks.remove(subtasks.get(oldSubtask.getID()));
            subtasks.remove(oldSubtask.getID());


            if (isValid) {
                subtasks.put(subtask.getID(), subtask);
                List<Subtask> list = epic.getSubtaskList();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getID() == subtask.getID()) {
                        list.add(i, subtask);
                        list.remove(i + 1);
                        break;
                    }
                }
                updateEpicTime(epic);
                if (subtask.getStartTime().isPresent()) {
                    sortedTasks.add(subtask);
                }
            } else {
                subtasks.put(oldSubtask.getID(), subtask);
                if (subtaskIsPresent) {
                    sortedTasks.add(oldSubtask);
                }
            }
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




