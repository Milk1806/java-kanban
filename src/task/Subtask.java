package task;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String name, String description, int epicId, String startTime, int minutes) {
        super(id, name, description, startTime, minutes);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, int epicId) {
        super(id, name, description);
        this.epicId = epicId;
    }

    public Subtask(int oldSubtaskID, String newName, String newDescription, int epicId, TaskStatus status) {
        super(oldSubtaskID, newName, newDescription, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "task.Subtask{" +
                "id=" + getID() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", epicID='" + epicId + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
