import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Subtask> subtaskList = new ArrayList<>();

    Epic(String name, String description, TaskManager taskManager) {
        super(name, description, taskManager);
    }

    Epic(int ID, String name, String description) {
        super(ID, name, description);
    }

    public void setSubtaskList(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "ID=" + ID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtaskList=" + subtaskList +
                '}';
    }
}
