package Managers;

import Tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head; // Указатель на первый элемент списка
    private Node tail; //Указатель на последний элемент списка
    protected Map<Long, Node> nodes = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public void remove(long id) {
        Node node = nodes.get(id);
        if (node != null) {
            removeNode(node);
            nodes.remove(id);
        }
    }

    public void linkLast(Task task) {
        if (nodes.containsKey(task.getId())) {
            remove(task.getId());
        }
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        nodes.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node node = head;
        while (node != null) {
            history.add(node.taskNode);
            node = node.next;
        }
        return history;
    }

    public void removeNode(Node node) {
        Node prev = node.prev;
        Node next = node.next;
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
        node.taskNode = null;
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "head=" + head +
                ", tail=" + tail +
                ", nodes=" + nodes +
                '}';
    }

    public static class Node {
        public Task taskNode;
        public Node next;
        public Node prev;

        public Node(Node prev, Task taskNode, Node next) {
            this.taskNode = taskNode;
            this.next = next;
            this.prev = prev;
        }
    }
}


