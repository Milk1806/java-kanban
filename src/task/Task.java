package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;
    private LocalDateTime startTime;
    private Duration duration;

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(int id, String name, String description, String startTime, int minutes) {
        this.id = id;
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
        this.startTime = LocalDateTime.parse(startTime);
        duration = Duration.ofMinutes(minutes);
    }

    public Task(int oldTaskID, String name, String description, TaskStatus status) {
        this.status = status;
        id = oldTaskID;
        this.description = description;
        this.name = name;
    }

    public Task(int oldTaskID, String name, String description, TaskStatus status,
                String startTime, int minutes) {
        this.status = status;
        id = oldTaskID;
        this.description = description;
        this.name = name;
        this.startTime = LocalDateTime.parse(startTime);
        duration = Duration.ofMinutes(minutes);
    }

    public Optional<LocalDateTime> getEndTime() {
        return getStartTime()
                .flatMap(startTime -> getDuration()
                .map(duration -> startTime.plusMinutes(duration.toMinutes())));
    }

    public Optional<LocalDateTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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
