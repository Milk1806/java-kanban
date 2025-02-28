package Tasks;

public class Subtask extends Task {
    private final int epicID;

    public Subtask(int ID, String name, String description, int epicID) {
        super(ID, name, description);
        this.epicID = epicID;
    }

    public Subtask(int oldSubtaskID, String newName, String newDescription, int epicID, TaskStatus status) {
        super(oldSubtaskID, newName, newDescription, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return "Tasks.Subtask{" +
                "ID=" + getID() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", epicID='" + epicID + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
