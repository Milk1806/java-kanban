package task;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Epic extends Task {
    private List<Subtask> subtaskList;
    private LocalDateTime endTime;

    public Epic(int id, String name, String description) {
        super(id, name, description);
        subtaskList = new ArrayList<>();
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description);
        subtaskList = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    @Override
    public String toString() {
        return "task.Epic{" +
                "id=" + getID() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskList=" + subtaskList +
                '}';
    }
}
