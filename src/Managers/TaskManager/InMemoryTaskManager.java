package Managers.TaskManager;

import Exceptions.TimeErrorException;
import Managers.History.HistoryManager;
import Managers.Managers;
import Tasks.Epic;
import Tasks.Sub;
import Tasks.Task;
import Tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected long generateId = 0;
    protected Map<Long, Task> singleTask = new HashMap<>();
    protected Map<Long, Sub> subTask = new HashMap<>();
    protected Map<Long, Epic> epicTask = new HashMap<>();
    protected static HistoryManager historyManager = Managers.getHistoryDefault();


    protected Map<LocalDateTime, Task> sortedPrioritization = new TreeMap<>();

    //Сортировка по приоритету
    public Map<LocalDateTime, Task> getSortedPrioritization() {
        return sortedPrioritization;
    }

    // Получаем список всех задач.
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(singleTask.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicTask.values());
    }

    @Override
    public List<Sub> getSubs() {
        return new ArrayList<>(subTask.values());
    }

    // Получение по ID
    @Override
    public Task getTask(long id) {
        Task task = singleTask.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpic(long id) {
        Epic epic = epicTask.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void deleteAll() {
        singleTask.clear();
        epicTask.clear();
        subTask.clear();
    }

    @Override
    public Sub getSub(long id) {
        Sub sub = subTask.get(id);
        if (sub != null) {
            historyManager.add(sub);
        }
        return sub;
    }

    // Создание

    @Override
    public void addTask(Task task) {
        Long id = idGenerate();
        searchIntersectionInTime(task);
        task.setId(id);
        singleTask.put(id, task);
        sortedPrioritization.put(task.getStart(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        Long id = idGenerate();
        searchIntersectionInTime(epic);
        epic.setId(id);
        epicTask.put(id, epic);
        sortedPrioritization.put(epic.getStart(), epic);
        updateEpicStatus(epic);
        timeDurationEpic(epic);
    }

    @Override
    public void addSubTask(Sub sub) {
        Long id = idGenerate();
        searchIntersectionInTime(sub);
        sub.setId(id);
        subTask.put(id, sub);
        sortedPrioritization.put(sub.getStart(), sub);
        Long epicId = sub.getEpicId();
        Epic epic = epicTask.get(epicId);
        epic.getSubTaskIds().add(sub);
        updateEpicStatus(epic);
        timeDurationEpic(epic);
    }

    //Подзадачи эпика
    @Override
    public List<Sub> getSubsByEpic(Epic epic) {
        return new ArrayList<>(epic.getSubTaskIds());
    }

    // Обновление
    @Override
    public void updateTask(Task task) {
        long id = task.getId();
        if (singleTask.containsKey(id)) {
            singleTask.put(id, task);
        }
        searchIntersectionInTime(task);
        sortedPrioritization.put(task.getStart(), task);
        task.getEnd();
    }

    @Override
    public void updateEpic(Epic epic) {
        long id = epic.getId();
        if (epicTask.containsKey(id)) {
            epicTask.put(id, epic);
        }
        searchIntersectionInTime(epic);
        sortedPrioritization.put(epic.getStart(), epic);
        timeDurationEpic(epic);
    }

    @Override
    public void updateSub(Sub sub) {
        Epic epic = epicTask.get(sub.getEpicId());
        if (Objects.isNull(epic)) {
            return;
        }
        epic.getSubTaskIds().remove(subTask.get(sub.getId()));
        Long id = sub.getId();
        if (subTask.containsKey(id)) {
            subTask.put(id, sub);
        }
        searchIntersectionInTime(sub);
        sortedPrioritization.put(sub.getStart(), sub);
        timeDurationEpic(epic);
        updateEpicStatus(epic);
    }

    // Генератор ID
    @Override
    public Long idGenerate() {
        return ++generateId;
    }

    // Удаление
    @Override
    public void deleteTaskByIdSingle(long id) {
        Task task = getTask(id);
        if (id == task.getId())
            sortedPrioritization.remove(task.getStart());
        singleTask.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteTaskByIdEpic(long id) {
        if (epicTask.containsKey(id)) {
            Epic epic = epicTask.get(id);
            for (Sub subTaskId : epic.getSubTaskIds()) {
                sortedPrioritization.remove(subTaskId.getStart());
                historyManager.remove(subTaskId.getId());
                subTask.remove(subTaskId.getId());
            }
            if (id == epic.getId())
                sortedPrioritization.remove(epic.getStart());
            epicTask.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteTaskByIdSubtask(long id) {
        Sub sub = getSub(id);
        subTask.remove(id);
        historyManager.remove(id);
        if (id == sub.getId()) {
            Epic epic = epicTask.get(sub.getEpicId());
            sortedPrioritization.remove(sub.getStart());
            timeDurationEpic(epic);
            updateEpicStatus(epic);
        }
    }

    // Обновление статуса Epic
    @Override
    public void updateEpicStatus(Epic epic) {
        int statusNew = 0;
        int statusDone = 0;
        List<Sub> sublist = new ArrayList<>(epic.getSubTaskIds());
        if (sublist.size() == 0) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            for (Sub value : sublist) {
                TaskStatus status = value.getStatus();
                if (status.equals(TaskStatus.NEW)) {
                    statusNew++;
                } else if (status.equals(TaskStatus.DONE)) {
                    statusDone++;
                }
            }
            if (statusNew == sublist.size()) {
                epic.setStatus(TaskStatus.NEW);
            } else if (statusDone == sublist.size()) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    //Продолжительность эпика
    @Override
    public void timeDurationEpic(Epic epic) {
        LocalDateTime localStart = LocalDateTime.MAX;
        LocalDateTime localEnd = LocalDateTime.MIN;
        for (Sub sub : getSubsByEpic(epic)) {
            if (localStart.isAfter(sub.getStart())) {
                localStart = sub.getStart();
            }
            if (localEnd.isBefore(sub.getStart().plusMinutes(sub.getDuration()))) {
                localEnd = sub.getStart().plusMinutes(sub.getDuration());
            }
            epic.setStart(localStart);
            epic.setEpicEnd(localEnd);
            epic.setDuration(Duration.between(localStart, localEnd).toMinutes());
        }
    }

    //Получение истории
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Поиск пересечений во времени
    @Override
    public void searchIntersectionInTime(Task task) {
        if (task.getStart() != null) {
            for (Task mapTask : sortedPrioritization.values()) {
                if (task.getStart().isBefore(mapTask.getEnd())
                        && task.getEnd().isAfter(mapTask.getStart())) {
                    throw new TimeErrorException("Обнаружено пересечение во времени");
                }
            }
        }
    }
}