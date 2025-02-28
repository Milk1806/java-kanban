package Tasks;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtaskList;

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

    @Override
    public String toString() {
        return "Tasks.Epic{" +
                "id=" + getID() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskList=" + subtaskList +
                '}';
    }
}
