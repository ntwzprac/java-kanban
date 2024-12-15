package Managers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private TasksLinkedList<Task> history;
    private Map<Integer, Node<Task>> nodeMap;

    public InMemoryHistoryManager() {
        history = new TasksLinkedList<Task>();
        nodeMap = new LinkedHashMap<>();
    }

    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            history.removeNode(nodeMap.get(task.getId()));
        }
        history.linkLast(task);
        nodeMap.put(task.getId(), history.tail);
    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            history.removeNode(nodeMap.get(id));
            nodeMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }
}

class TasksLinkedList<T> {
    public Node<T> head;
    public Node<T> tail;
    private int size = 0;

    public int getSize() {
        return size;
    }

    public void linkLast(T task) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<T>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;
    }

    public void removeNode(Node<T> node) {
        Node<T> prev = node.prev;
        Node<T> next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.data = null;
        size--;
    }

    public List<T> getTasks() {
        List<T> history = new ArrayList<>();
        Node<T> node = head;
        while (node != null) {
            history.add(node.data);
            node = node.next;
        }
        return history;
    }
}