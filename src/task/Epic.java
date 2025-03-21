package task;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtaskList;

    public Epic(int id, String name, String description) {
        super(id, name, description);
        subtaskList = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
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
