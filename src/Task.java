public class Task {
    final String name;
    final String description;
    final int ID;
    TaskStatus status;

    Task(String name, String description, TaskManager taskManager) {
        this.name = name;
        this.description = description;
        ID = taskManager.getID();
        status = TaskStatus.NEW;
    }

    public Task(int ID, String name, String description, TaskStatus status) {
        this.status = status;
        this.ID = ID;
        this.description = description;
        this.name = name;
    }

    public Task(int ID, String name, String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "ID=" + ID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
