package manager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> tasksHistory;
    private Map<Integer, Node<Task>> map;
    private Node<Task> first;
    private Node<Task> last;

    public InMemoryHistoryManager() {
        tasksHistory = new ArrayList<>();
        map = new LinkedHashMap<>();
    }

    @Override
    public void add(Task task) {
       if (map.containsKey(task.getID())) {
           removeNode(map.get(task.getID()));
       }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        tasksHistory.clear();
        getTasks();
        return tasksHistory;
    }

    @Override
    public void remove(int id) {
        if (map.containsKey(id)) {
            map.remove(id);
        }
    }

    class Node <Task> {
        public Task task;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(Node<Task> prev, Task task, Node<Task> next) {
            this.task = task;
            next = null;
            prev = null;
        }
    }

    void linkLast(Task task) {
        final Node<Task> oldLast = last;
        final Node<Task> newNode = new Node<>(oldLast, task, null);
        last = newNode;
        if (oldLast == null) {
            first = newNode;
            map.put(task.getID(), first);
        } else {
            oldLast.next = newNode;
            last.prev = oldLast;
            map.put(task.getID(), oldLast.next);
        }

    }

    void getTasks() {
        map.forEach((integer, taskNode) -> tasksHistory.add(taskNode.task));
    }

    void removeNode(Node<Task> node) {
        Node<Task> next = node.next;
        Node<Task> prev = node.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            next.prev = prev;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            prev.next = next;
        }
        map.remove(node.task.getID());
    }
}


