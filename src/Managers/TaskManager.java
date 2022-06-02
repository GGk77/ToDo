package Managers;

import Tasks.Epic;
import Tasks.Sub;
import Tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskManager {

    // Получаем список всех задач.
    List<Task> getTasks();

    List<Epic> getEpics();

    List<Sub> getSubs();

    // Получение по ID
    Task getTask(long id);

    Epic getEpic(long id);

    Sub getSub(long id);

    // Создание
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(Sub sub);

    //Получение подзадач эпика
    List<Sub> getSubsByEpic(Epic epic);


    // Обновление
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSub(Sub sub);

    // Генератор ID
    Long idGenerate();

    // Удаление
    void deleteTaskByIdSingle(long id);

    void deleteTaskByIdEpic(long id);

    void deleteTaskByIdSubtask(long id);

    // Обновление статуса Epic
    void updateEpicStatus(Epic epic);

    //Продолжительность эпика
    void timeDurationEpic(Epic epic);

    // Получение истории
    List<Task> getHistory();

    // Поиск пересечений во времени
    void searchIntersectionInTime(Task task);
}