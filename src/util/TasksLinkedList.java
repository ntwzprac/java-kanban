package util;

import java.util.ArrayList;
import java.util.List;

public class TasksLinkedList<T> {
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
            if (head != null) head.prev = null;
        } else {
            prev.next = next;
        }

        if (next == null) {
            tail = prev;
            if (tail != null) tail.next = null;
        } else {
            next.prev = prev;
        }

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
