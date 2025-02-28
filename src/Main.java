import Manager.TaskManager;
import Tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.addTask(new Task(taskManager.getID(), "1", "1"));
        taskManager.addTask(new Task(taskManager.getID(), "2", "2"));
        taskManager.addEpic(new Epic(taskManager.getID(), "Масштабная - 1", "Масштабная - 1"));
        taskManager.addSubtask(new Subtask(taskManager.getID(), "$", "$", 3));
        taskManager.addSubtask(new Subtask(taskManager.getID(), "%", "%", 3));
        taskManager.addEpic(new Epic(taskManager.getID(), "Масштабная - 2", "Масштабная - 2"));
        taskManager.addSubtask(new Subtask(taskManager.getID(), "-", "-", 6));

        System.out.println();
        System.out.println(taskManager.getTasks());
        System.out.println();
        System.out.println(taskManager.getEpics());
        System.out.println();
        System.out.println(taskManager.getSubtasks());

        taskManager.updateTask(new Task(1, "111", "111", TaskStatus.DONE));
        taskManager.updateTask(new Task(2, "222", "222", TaskStatus.IN_PROGRESS));
        taskManager.updateSubtask(new Subtask(4, "$$$", "$$$", 3, TaskStatus.IN_PROGRESS));
        taskManager.updateSubtask(new Subtask(5, "%%%", "%%%", 3, TaskStatus.DONE));
        taskManager.updateEpic(new Epic(3, "МАСШТАБНАЯ - 1", "МАСШТАБНАЯ - 1"));
        taskManager.updateSubtask(new Subtask(7, "----", "----", 6, TaskStatus.DONE));
        taskManager.updateEpic(new Epic(6, "МАСШТАБНАЯ - 2", "МАСШТАБНАЯ - 2"));
        taskManager.removeTaskOnID(1);
        taskManager.removeEpicOnID(3);

        System.out.println();
        System.out.println(taskManager.getTasks());
        System.out.println();
        System.out.println(taskManager.getEpics());
        System.out.println();
        System.out.println(taskManager.getSubtasks());
    }
}
