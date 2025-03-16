package manager;

import task.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> tasksHistory;

    public InMemoryHistoryManager() {
        tasksHistory = new ArrayList<>(10);
    }

    @Override
    public void add(Task task) {
        if (tasksHistory.isEmpty() || tasksHistory.size() < 10) {
            tasksHistory.add(task);
        } else if (tasksHistory.size() == 10) {
            tasksHistory.removeFirst();
            tasksHistory.addLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory;
    }
}
