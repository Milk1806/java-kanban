public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.addTask(new Task("1", "1", taskManager));
        taskManager.addTask(new Task("2", "2", taskManager));
        taskManager.addEpic(new Epic("Масштабная - 1", "Масштабная - 1", taskManager));
        taskManager.addSubtask(new Subtask(3, "$$$$", "$$$$", taskManager));
        taskManager.addSubtask(new Subtask(3, "%%%%", "%%%%", taskManager));
        taskManager.addEpic(new Epic("Масштабная - 2", "Масштабная - 2", taskManager));
        taskManager.addSubtask(new Subtask(6, "-----", "-----", taskManager));

        System.out.println();
        System.out.println(taskManager.getTasks());
        System.out.println();
        System.out.println(taskManager.getEpics());
        System.out.println();
        System.out.println(taskManager.getSubtasks());

        taskManager.updateTask(new Task(1, "11", "11", TaskStatus.DONE));
        taskManager.updateTask(new Task(2, "22", "22", TaskStatus.IN_PROGRESS));
        taskManager.updateSubtask(new Subtask(3, 4, "$", "$", TaskStatus.IN_PROGRESS));
        taskManager.updateSubtask(new Subtask(3, 5, "%", "%", TaskStatus.DONE));
        taskManager.updateEpic(new Epic(3, "МАСШТАБНАЯ - 1", "МАСШТАБНАЯ - 1"));
        taskManager.updateSubtask(new Subtask(6, 7, "^^^^^^", "^^^^^", TaskStatus.DONE));
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
