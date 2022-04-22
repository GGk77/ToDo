package Managers;

import Tasks.Epic;
import Tasks.Sub;
import Tasks.Task;
import Tasks.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    long generateId = 0;

    Map<Long, Task> singleTask = new HashMap<>();
    Map<Long, Sub> subTask = new HashMap<>();
    Map<Long, Epic> epicTask = new HashMap<>();

    private final HistoryManager historyManager = Managers.getHistoryDefault();

    // Получаем список всех задач.
    @Override
    public List<Task> getTasks() {
        return new ArrayList<Task>(singleTask.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<Epic>(epicTask.values());
    }

    @Override
    public List<Sub> getSubs() {
        return new ArrayList<Sub>(subTask.values());
    }

    @Override
    public Task getTask(long id) {
        Task task = singleTask.get(id);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public Epic getEpic(long id) {
        Epic epic = epicTask.get(id);
        historyManager.addTask(epic);
        return epic;
    }

    @Override
    public Sub getSub(long id) {
        Sub sub = subTask.get(id);
        historyManager.addTask(sub);
        return sub;
    }

    // Создание

    @Override
    public Long addTask(Task task) {
        Long id = idGenerate();
        task.setId(id);
        singleTask.put(id, task);
        return id;
    }

    @Override
    public Long addEpic(Epic epic) {
        Long id = idGenerate();
        epic.setId(id);
        epicTask.put(id, epic);
        updateEpicStatus(id);
        return id;
    }

    @Override
    public Long addSubTask(Sub sub) {
        Long id = idGenerate();
        sub.setId(id + 1);
        subTask.put(id, sub);
        Long epicId = sub.getEpicId();
        Epic epic = epicTask.get(epicId);
        epic.getSubTaskIds().add(id);
        return id;
    }


    // Получение по ID

    @Override
    public void getTaskById(long id) {
        if (singleTask.containsKey(id)) {
            Task ID = singleTask.get(id);
            System.out.println(ID);

        } else if (epicTask.containsKey(id)) {
            Epic ID = epicTask.get(id);
            System.out.println(ID);

        } else if (subTask.containsKey(id)) {
            Sub ID = subTask.get(id);
            System.out.println(ID);

        } else {
            System.out.println("Задачи с таким Id нет");
        }
    }

    // Получение подзадач эпика

    @Override
    public List<Sub> getSubtaskEpic(long epicId) {
        List<Long> subListId = epicTask.get(epicId).getSubTaskIds();
        List<Sub> subList = new ArrayList<>();
        for (long sub : subListId) {
            subList.add(subTask.get(sub));

        }
        return subList;
    }

    // Обновление

    @Override
    public void updateTask(Task task) {
        long id = task.getId();
        if (singleTask.containsKey(id)) {
            singleTask.put(id, task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        long id = epic.getId();
        if (epicTask.containsKey(id)) {
            epicTask.put(id, epic);
        }

    }

    @Override
    public void updateSub(Sub sub) {
        Long epicId = sub.getEpicId();
        if (Objects.isNull(epicId)) {
            return;
        }
        if (epicTask.containsKey(epicId)) {
            return;
        }
        Long id = sub.getId();
        if (subTask.containsKey(id)) {
            subTask.put(id, sub);
        }
        updateEpicStatus(sub.getEpicId());
    }

    // Генератор ID

    @Override
    public Long idGenerate() {
        return ++generateId;
    }

    // Удаление

    @Override
    public void removeAllTasks() {
        singleTask.clear();
        subTask.clear();
        epicTask.clear();

        System.out.println("Ничего нет");
    }

    @Override
    public void deleteTaskByIdSingle(long id) {
        singleTask.remove(id);
        getHistory().remove(id);

    }

    @Override
    public void deleteTaskByIdEpic(long id) {
        if (epicTask.containsKey(id)) {
            List<Long> idEpic = epicTask.get(id).getSubTaskIds();
            for (Long subTaskId : idEpic) {
                subTask.remove(subTaskId);
                historyManager.remove(subTaskId);
            }
            epicTask.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteTaskByIdSubtask(long id) {
        subTask.remove(id);
        getHistory().remove(id);
        historyManager.remove(id);
    }

    // Обновление статуса Epic

    @Override
    public void updateEpicStatus(long epicId) {
        int statusNew = 0;
        int statusDone = 0;
        List<Long> list;
        list = epicTask.get(epicId).getSubTaskIds();
        if (list.size() == 0) {
            epicTask.get(epicId).setStatus(TaskStatus.NEW);
        } else {
            for (Long value : list) {
                TaskStatus status = subTask.get(value).getStatus();
                if (status.equals(TaskStatus.NEW)) {
                    statusNew++;
                } else if (status.equals(TaskStatus.DONE)) {
                    statusDone++;
                }
            }
            if (statusNew == list.size()) {
                epicTask.get(epicId).setStatus(TaskStatus.NEW);
            } else if (statusDone == list.size()) {
                epicTask.get(epicId).setStatus(TaskStatus.DONE);
            } else {
                epicTask.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}