package Tasks;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Subtask> subtaskList;

    public Epic(int oldEpicID, String name, String description) {
        super(oldEpicID, name, description);
        subtaskList = new ArrayList<>();
    }

    public void setSubtaskList(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Tasks.Epic{" +
                "ID=" + ID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtaskList=" + subtaskList +
                '}';
    }
}
