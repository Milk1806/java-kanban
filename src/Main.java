import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        manager.addTask(new Task(manager.getNewID(), "1", "1"));
        manager.addTask(new Task(manager.getNewID(), "2", "2"));
        manager.addEpic(new Epic(manager.getNewID(), "3", "3"));
        manager.addEpic(new Epic(manager.getNewID(), "4", "4"));
        manager.addSubtask(new Subtask(manager.getNewID(), "333", "333", 3));
        manager.addSubtask(new Subtask(manager.getNewID(), "3-3-3", "3-3-3", 3));
        manager.addSubtask(new Subtask(manager.getNewID(), "3^3^3", "3^3^3", 3));



        manager.getTaskByld(1);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getEpicByld(3);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getSubtaskByld(5);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getTaskByld(2);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getEpicByld(4);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getSubtaskByld(6);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getSubtaskByld(7);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getTaskByld(1);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getTaskByld(2);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getEpicByld(3);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getEpicByld(4);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getSubtaskByld(5);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getSubtaskByld(6);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));
        manager.getSubtaskByld(7);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));

        manager.removeSubtaskOnID(7);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));

        manager.removeEpicOnID(3);
        System.out.println(manager.getHistory());
        System.out.println("-".repeat(150));

    }
}
