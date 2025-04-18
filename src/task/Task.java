package task;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(String name, String description, int epicID) {
        this.name = name;
        this.description = description;
        this.id = epicID;
    }

    public Task(int oldTaskID, String name, String description, TaskStatus status) {
        this.status = status;
        id = oldTaskID;
        this.description = description;
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "task.Task{" +
                "id=" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Task task)) return false;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }
}
