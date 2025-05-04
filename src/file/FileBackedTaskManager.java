package file;

import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path path;

    public FileBackedTaskManager() {
        super();
        path = Paths.get("autosave.csv");
    }

    public Path getPath() {
        return path;
    }

    public void save() {
        List<Task> allTasksList = new ArrayList<>();
            allTasksList.addAll(tasks.values());
            allTasksList.addAll(epics.values());
            allTasksList.addAll(subtasks.values());

        try (Writer writer = new FileWriter(path.toFile())) {
            writer.write("id,type,name,status,description,epic,startTime,duration,endTime\n");
            for (Task task : allTasksList) {
                String str = toString(task);
                if (str == null) {
                    continue;
                }
                writer.write(str);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при работе с файлом");
        }
    }

    public String toString(Task task) {
        String typeOfTask;

        switch (task) {
            case Epic epic -> {
                typeOfTask = String.valueOf(TypesOfTask.EPIC);
                return epic.getID() + "," + typeOfTask + "," + epic.getName() + "," + epic.getStatus()
                        + "," + epic.getDescription() + "," + epic.getStartTime().orElse(null) + ","
                        + epic.getDuration().orElse(null) + ","
                        + epic.getEndTime().orElse(null) + "\n";
            }
            case Subtask subtask -> {
                typeOfTask = String.valueOf(TypesOfTask.SUBTASK);
                return subtask.getID() + "," + typeOfTask + "," + subtask.getName() + "," + subtask.getStatus()
                        + "," + subtask.getDescription() + "," + subtask.getEpicId() + ","
                        + subtask.getStartTime().orElse(null) + ","
                        + subtask.getDuration().orElse(null) + ","
                        + subtask.getEndTime().orElse(null) + "\n";
            }
            default -> {
                typeOfTask = String.valueOf(TypesOfTask.TASK);
                return task.getID() + "," + typeOfTask + "," + task.getName() + "," + task.getStatus()
                        + "," + task.getDescription() + "," + task.getStartTime().orElse(null) + ","
                        + task.getDuration().orElse(null) + ","
                        + task.getEndTime().orElse(null) + "\n";
            }
        }
    }

    public Task fromString(String value) {
        String[] taskFields = value.trim().split(",");

        if (taskFields[1].equals(TypesOfTask.TASK.toString())) {
            Task task = new Task(Integer.parseInt(taskFields[0]), taskFields[2], taskFields[4],
                    TaskStatus.valueOf(taskFields[3]));
                try {
                    task.setStartTime(LocalDateTime.parse(taskFields[5]));
                    task.setDuration(Duration.parse(taskFields[6]));
                } catch (DateTimeParseException e) {
                    task.setStartTime(null);
                    task.setDuration(null);
                }
            return task;
        } else if (taskFields[1].equals(TypesOfTask.EPIC.toString())) {
            Epic epic = new Epic(Integer.parseInt(taskFields[0]), taskFields[2], taskFields[4],
                    TaskStatus.valueOf(taskFields[3]));
                try {
                    epic.setStartTime(LocalDateTime.parse(taskFields[5]));
                    epic.setDuration(Duration.parse(taskFields[6]));
                    epic.setEndTime(LocalDateTime.parse(taskFields[7]));
                } catch (DateTimeParseException e) {
                    epic.setStartTime(null);
                    epic.setDuration(null);
                    epic.setEndTime(null);
                }
            return epic;
        } else if (taskFields[1].equals(TypesOfTask.SUBTASK.toString())) {
            Subtask subtask = new Subtask(Integer.parseInt(taskFields[0]), taskFields[2], taskFields[4],
                    Integer.parseInt(taskFields[5]), TaskStatus.valueOf(taskFields[3]));
                try {
                    subtask.setStartTime(LocalDateTime.parse(taskFields[6]));
                    subtask.setDuration(Duration.parse(taskFields[7]));
                } catch (DateTimeParseException e) {
                    subtask.setStartTime(null);
                    subtask.setDuration(null);
                }
            return subtask;
        } else {
            return null;
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBacked = new FileBackedTaskManager();

        try {
            List<String> list = Files.readAllLines(file.toPath());
            for (String string : list) {
                Task task = fileBacked.fromString(string);

                if (task instanceof Epic) {
                    fileBacked.epics.put(task.getID(), (Epic) task);
                } else if (task instanceof Subtask subtask) {
                    fileBacked.subtasks.put(task.getID(), subtask);
                    fileBacked.epics.get(subtask.getEpicId()).addSubtask(subtask);
                } else if (task instanceof Task) {
                    fileBacked.tasks.put(task.getID(), task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при работе с файлом");
        }
        return fileBacked;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void removeTaskOnID(int taskID) {
        super.removeTaskOnID(taskID);
        save();
    }

    @Override
    public void removeEpicOnID(int epicID) {
        super.removeEpicOnID(epicID);
        save();
    }

    @Override
    public void removeSubtaskOnID(int subtaskID) {
        super.removeSubtaskOnID(subtaskID);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }


}
