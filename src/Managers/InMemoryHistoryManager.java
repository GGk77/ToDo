package Managers;

import Tasks.Task;


import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Long, Node> nodes = new HashMap<>();
    private Node head;
    private Node tail;


    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node node = head;
        while (node != null) {
            historyList.add(node.getTask());
            node = node.next;
        }
        return historyList;
    }

    @Override
    public void addTask(Task task) {
        if (Objects.isNull(task)) {
            return;
        }
        final Long id = task.getId();
        removeNode(id);
        linkLast(task);
        nodes.put(id, tail);
    }

    @Override
    public void remove(Long id) {
            removeNode(id);

    }

    private void linkLast(Task task) {
        final Node node = new Node(task, null, tail);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }

    private void removeNode(Long id) {
        Node node = this.nodes.get(id);
        if (node != null) {
            if (node.next == null) {
                tail = node.prev;

            } else if (node.prev == null) {
                head = node.next;

            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;

            }
        }
    }

    private static class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Task task, Node next, Node prev) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        public Task getTask() {
            return task;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + task +
                    ", next=" + next +
                    ", prev=" + prev +
                    '}';
        }
    }
}
