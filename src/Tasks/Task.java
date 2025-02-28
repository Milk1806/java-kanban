package Tasks;
import Manager.TaskManager;

public class Task {
    protected String name;
    protected String description;
    protected int ID;
    TaskStatus status;

    public Task(int ID, String name, String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
    }

    public Task(String name, String description, int epicID) {
        this.name = name;
        this.description = description;
        this.ID = epicID;
    }

    public Task(int oldTaskID, String name, String description, TaskStatus status) {
        this.status = status;
        ID = oldTaskID;
        this.description = description;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "Tasks.Task{" +
                "ID=" + ID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
