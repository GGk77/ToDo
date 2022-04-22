package Managers;

import Tasks.Task;

import java.util.List;


public interface HistoryManager {

    List<Task> getHistory();

    void addTask(Task task);
    void remove(Long id);

}
