package file;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node<Task>> map;
    private Node<Task> first;
    private Node<Task> last;

    public InMemoryHistoryManager() {
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
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(map.get(id));
    }

    class Node<T> {
        public T task;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, T task, Node<T> next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
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

    List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        if (first != null) {
            list.add(first.task);
            Node<Task> node = first.next;
            while (node != null) {
                list.add(node.task);
                node = node.next;
            }
        }
        return list;
    }

    void removeNode(Node<Task> node) {
        Node<Task> next = node.next;
        Node<Task> prev = node.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        map.remove(node.task.getID());
        node.task = null;
    }
}


