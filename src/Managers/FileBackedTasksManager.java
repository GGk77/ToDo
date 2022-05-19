package Managers;

import Tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Tasks.TaskType.*;


public class FileBackedTasksManager extends InMemoryTaskManager {
    protected HistoryManager historyManager = Managers.getHistoryDefault();
    private final File file;

    //Конструкторы
//    public FileBackedTasksManager() {
//        this(new File("task.csv"), false);
//    }

    public FileBackedTasksManager(File file) {
        this(file, false);
    }

    public FileBackedTasksManager(File file, boolean load) {
        this.file = file;
        if (load) {
            load();
        }
    }

    @Override
    public List<Task> getTasks() {
        save();
        return super.getTasks();

    }

    @Override
    public List<Epic> getEpics() {
        save();
        return super.getEpics();
    }

    @Override
    public List<Sub> getSubs() {
        save();
        return super.getSubs();
    }

    @Override
    public Task getTask(long id) {
        save();
        return super.getTask(id);
    }

    @Override
    public Epic getEpic(long id) {
        save();
        return super.getEpic(id);
    }

    @Override
    public Sub getSub(long id) {
        save();
        return super.getSub(id);
    }

    @Override
    public Long addTask(Task task) {
        save();
        return super.addTask(task);

    }

    @Override
    public Long addEpic(Epic epic) {
        save();
        return super.addEpic(epic);

    }

    @Override
    public Long addSubTask(Sub sub) {
        save();
        return super.addSubTask(sub);

    }

    @Override
    public void getTaskById(long id) {
        save();
        super.getTaskById(id);
    }

    @Override
    public List<Sub> getSubtaskEpic(long epicId) {
        save();
        return super.getSubtaskEpic(epicId);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSub(Sub sub) {
        super.updateSub(sub);
        save();
    }

    @Override
    public Long idGenerate() {
        save();
        return super.idGenerate();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void deleteTaskByIdSingle(long id) {
        super.deleteTaskByIdSingle(id);
        save();
    }

    @Override
    public void deleteTaskByIdEpic(long id) {
        super.deleteTaskByIdEpic(id);
        save();
    }

    @Override
    public void deleteTaskByIdSubtask(long id) {
        super.deleteTaskByIdSubtask(id);
        save();
    }

    @Override
    public void updateEpicStatus(long epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public List<Task> getHistory() {
        save();
        return super.getHistory();
    }

    // Сохранение в файл
    private void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic"); // заголовок
            writer.newLine();
            // задачи, подзадачи, эпики, история
            for (Map.Entry<Long, Task> entry : singleTask.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Long, Epic> entry : epicTask.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Long, Sub> entry : subTask.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            writer.newLine();
            writer.append(historyToString(historyManager)); // TODO Вроде всё верно
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }

    // Восстановление из файла
    private void load() {
        long maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine(); // Пропускаем заголовок
            while (true) {
                String line = reader.readLine();
                // Задачи
                final Task task = fromString(line);
                // добавить задачу в менеджер
                final Long id = task.getId();
                TaskType type = task.getType();
                switch (type) {
                    case TASK:
                        singleTask.put(id, task);
                    case SUBTASK:
                        assert task instanceof Sub;
                        Sub sub = (Sub) task;
                        subTask.put(id, sub);
                        Epic epic = epicTask.get(sub.getEpicId());
                        epic.getSubTaskIds().add(id);
                    case EPIC:
                        assert task instanceof Epic;
                        epicTask.put(id, (Epic) task);

                }
                if (maxId < id) {
                    maxId = id;
                }
                if (line.isEmpty()) {
                    break;
                }
            }
            // История
            String historyLine = reader.readLine();
            List<Integer> taskHistory = historyFromString(historyLine);
            getTasks(taskHistory);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки");
        }
        generateId = ++maxId; // TODO правильно ли передал?
    }

    private void getTasks(List<Integer> taskHistory) {
        System.out.println(taskHistory);
    }


    public static void loadFromFile(File file) {
        final FileBackedTasksManager manager = new FileBackedTasksManager(file, true);
        manager.load();
    }

    // Сохранение задачи в строку
    private String toString(Task task) {
        TaskType type = task.getType();
        String ts = task.getId() + ", " + type + ", " + task.getName() + ", " + task.getStatus() + ", "
                + task.getDescription();
        switch (type) {
            case TASK:
                return ts;
            case SUBTASK:
                return ts;// + ", " + sub.getEpicId();
            // TODO ID эпика возможно не правильный вызов(последний)!!!
            case EPIC:
                return ts;
            default:
                throw new ManagerSaveException("Задача отсутствует");
        }
    }

    //Создание задачи из строки
    private Task fromString(String value) {
        final String[] values = value.split(", ");
        // id,type,name,status,description,epic
        TaskType type = valueOf(values[1]);
        String name = values[2];
        TaskStatus status = TaskStatus.valueOf(values[3]);
        String description = values[4];
        switch (type) {
            case TASK:
                return new Task(name, description, status, type);
            case SUBTASK:
                long epicId = Integer.parseInt(values[5]);
                return new Sub(name, description, status, type, epicId);
            case EPIC:
                return new Epic(name, description, status, type);
            default:
                throw new ManagerSaveException("Неверено введена задача");
        }
    }

    // Сохранение истории в строчку
    private String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(", ");
        }
        return sb.toString();
    }

    //  Восстановление истории
    static List<Integer> historyFromString(String value) {
        final String[] id = value.split(",");
        List<Integer> history = new ArrayList<>();
        for (String i : id) {
//            history.add(Integer.parseInt(i)); Если задач будет больше 128
            history.add(Integer.valueOf(i));
        }
        return history;
    }
}