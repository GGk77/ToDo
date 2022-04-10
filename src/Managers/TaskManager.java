package Managers;

import Tasks.Epic;
import Tasks.Sub;
import Tasks.Task;

import java.util.List;

public interface TaskManager {

    // Получаем список всех задач.
    List<Task> getTasks();
    List<Epic> getEpics();
    List<Sub> getSubs();
    Task getTask(long id);
    Epic getEpic(long id);
    Sub getSub(long id);

    // Создание
    Long addTask(Task task);
    Long addEpic(Epic epic);
    Long addSubTask(Sub sub);

    // Получение по ID
    void getTaskById(long id);

    // Получение подзадач эпика
    List<Sub> getSubtaskEpic(long epicId);

    // Обновление
    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSub(Sub sub);

    // Генератор ID
    Long idGenerate();

    // Удаление
    void removeAllTasks();
    void deleteTaskByIdSingle(long id);
    void deleteTaskByIdEpic(long id);
    void deleteTaskByIdSubtask(long id);

    // Обновление статуса Epic
    void updateEpicStatus(long epicId);

    // Получение истории
    List<Task>getHistory();
}
