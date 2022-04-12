package Managers;

import Tasks.Task;


import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager  implements HistoryManager {

    private final long LENGTH = 10;

    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void addTask(Task task) {
        if(Objects.isNull(task)) {
            return;
        }
        history.add(task);
        if (history.size() > LENGTH) {
            history.removeFirst();
        }
    }
}
