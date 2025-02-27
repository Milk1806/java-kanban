public class Subtask extends Task {
    int epicID;

    Subtask(int epicID, String name, String description, TaskManager taskManager) {
        super(name, description, taskManager);
        this.epicID = epicID;
    }

    public Subtask(int epicID, int ID, String name, String description, TaskStatus status) {
        super(ID, name, description, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }



    @Override
    public String toString() {
        return "Subtask{" +
                "ID=" + ID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
