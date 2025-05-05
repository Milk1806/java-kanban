import file.FileBackedTaskManager;
import file.Managers;
import file.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager manager = new FileBackedTaskManager();
        manager.addTask(new Task(manager.getNewID(), "1","1"
                ));
        manager.addEpic(new Epic(manager.getNewID(), "2","2"));
        manager.addSubtask(new Subtask(manager.getNewID(), "2-2-2","2-2-2",2
                ,"2025-05-02T12:00",60));
        manager.addSubtask(new Subtask(manager.getNewID(), "3-3-3","3-3-3",2
                ,"2025-05-02T11:30",60));
        manager.addSubtask(new Subtask(manager.getNewID(), "4-4-4","4-4-4",2
                ,"2025-05-03T21:00",80));
        manager.updateSubtask(new Subtask(5, "&&&&&&","&&&&&&",2
               ));
        System.out.println(manager.getPrioritizedTasks());
//        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(Paths.get("autosave.csv").toFile());
//        System.out.println(manager.getTasks());
//        System.out.println(manager.getEpics());
//        System.out.println(manager.getSubtasks());

    }
}
