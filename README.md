# Менеджер задач

**В данной программе реализовано:**

Программа хранит в себе задачи/подзадачи/эпики.
Каждая задача имеет:

1. Название;
2. Краткое описание;
3. Уникальный идентификационный номер;
4. Статус (New, In_progress, Done);
5. Подзадачи имеют индекс эпика, которому они принадлежат;
6. Продолжительность выполнения;
7. Ориентировочное время начала выполнения;

Программа имеет следующие функции:

1. Создать задачу/подзадачу/эпик;
2. Обновить задачу/подзадачу/эпик;
3. Получить список всех задач/подзадач/эпиков;
4. Получить задачу/подзадачу/эпик по идентификатору;
5. Удалить все задачи/подзадачи/эпики;
6. Удалить задачу/подзадачу/эпик по идентификатору;
7. Получить список всех подзадач эпика;
8. Посмотреть историю просмотра задач;
9. Запись (чтение) задач/подзадач/эпиков в (из) файл(а);
10. Сортировка списка задач по времени;

Для хранения истории просмотров задач реализованы методы, позволяющие реализовать алгоритмическую сложность О(1).
Это достигается реализацией двусвязанного списка и HashMap.

Доступ к методам осуществляется с помощью HTTP-запросов и хранит свое состояние на отдельном сервере.

Программа написана на Java. Пример кода:

```java
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

    // Получение по ID
    @Override
    public Task getTask(long id) {
        Task task = singleTask.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    // Обновление статуса
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
}
```
