package Managers;

import Tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FileBackedTasksManager extends InMemoryTaskManager {
    //    public static void main(String[] args) {
////        TaskManager manager = Managers.getDefault();
//        TaskManager manager1 = Managers.getFileBacked();
//        //Создание задач
//        Long task1 = manager1.addTask(new Task("Task1", "Сходить на работу", TaskStatus.NEW, TaskType.TASK));
//        Long task2 = manager1.addTask(new Task("Task2", "Сходить на работу", TaskStatus.NEW, TaskType.TASK));
//        Long task3 = manager1.addTask(new Task("Task3", "Сходить на работу", TaskStatus.NEW, TaskType.TASK));
//        Long task4 = manager1.addTask(new Task("Task4", "Сходить на работу", TaskStatus.NEW, TaskType.TASK));
//        //Создание эпиков
//        Long epic1 = manager1.addEpic(new Epic("Epic1", "Помыть машину", TaskStatus.NEW, TaskType.EPIC));
//        Long epic2 = manager1.addEpic(new Epic("Epic2", "Помыть машину", TaskStatus.NEW, TaskType.EPIC));
//        Long epic3 = manager1.addEpic(new Epic("Epic3", "Помыть машину", TaskStatus.NEW, TaskType.EPIC));
//        Long epic4 = manager1.addEpic(new Epic("Epic4", "Помыть машину", TaskStatus.NEW, TaskType.EPIC));
//        //Создание подзадачи к эпику
//        Long sub1_1 = manager1.addSubTask(new Sub("Subtask1", "SUB 1 к эпику 1", TaskStatus.NEW, TaskType.SUBTASK, epic1));
//        Long sub2_1 = manager1.addSubTask(new Sub("Subtask2", "SUB 2 к эпику 1", TaskStatus.NEW, TaskType.SUBTASK, epic1));
//        Long sub3_1 = manager1.addSubTask(new Sub("Subtask3", "SUB 3 к эпику 1", TaskStatus.NEW, TaskType.SUBTASK, epic1));
//        Long sub1_4 = manager1.addSubTask(new Sub("Subtask3", "SUB 1 к эпику 4", TaskStatus.NEW, TaskType.SUBTASK, epic4));
////       //Вызов некоторых задач, эпиков и подзадач
////       //Задачи
//        manager1.getTask(task1);
//        manager1.getTask(task3);
////        //Эпики
//        manager1.getEpic(epic1);
//        manager1.getEpic(epic4);
////        //Подзадача
//        manager1.getSub(sub1_1);
//        manager1.getSub(sub1_4);
//        printAllTask(manager1);
//        FileBackedTasksManager.loadFromFile(new File("taskPrinter.csv"));
//    }
//    public static void printAllTask(TaskManager manager) {
//        System.out.println("Task");
//        for (Task task : manager.getTasks()) System.out.println(task);
//        System.out.println("Epic");
//        for (Epic epic : manager.getEpics()) System.out.println(epic);
//        System.out.println("Sub");
//        for (Sub sub : manager.getSubs()) System.out.println(sub);
//        System.out.println("History");
//        for (Task task : manager.getHistory()) System.out.println(task);
//    }
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
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
    public void addTask(Task task) {
        save();
        super.addTask(task);
    }

    @Override
    public void addEpic(Epic epic) {
        save();
        super.addEpic(epic);
    }

    @Override
    public void addSubTask(Sub sub) {
        save();
        super.addSubTask(sub);
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
    public List<Sub> getSubsByEpic(Epic epic) {
        return super.getSubsByEpic(epic);
    }

    @Override
    public Long idGenerate() {
        save();
        return super.idGenerate();
    }


    @Override
    public void deleteTaskByIdSingle(long id) {
        save();
        super.deleteTaskByIdSingle(id);
    }

    @Override
    public void deleteTaskByIdEpic(long id) {
        save();
        super.deleteTaskByIdEpic(id);
    }

    @Override
    public void deleteTaskByIdSubtask(long id) {
        save();
        super.deleteTaskByIdSubtask(id);
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        save();
        super.updateEpicStatus(epic);
    }

    @Override
    public Map<LocalDateTime, Task> getSortedPrioritization() {
        return super.getSortedPrioritization();
    }

    @Override
    public void timeDurationEpic(Epic epic) {
        super.timeDurationEpic(epic);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();

    }

    @Override
    public void searchIntersectionInTime(Task task) {
        super.searchIntersectionInTime(task);
        save();
    }

    // Сохранение в файл
    protected void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic" + "\n"); // заголовок
            List<String> ls = new ArrayList<>();
            // задачи, подзадачи, эпики, история
            for (Map.Entry<Long, Task> entry : singleTask.entrySet()) {
                ls.add((toString(entry.getValue())));
            }
            for (Map.Entry<Long, Epic> entry : epicTask.entrySet()) {
                ls.add((toString(entry.getValue())));
            }
            for (Map.Entry<Long, Sub> entry : subTask.entrySet()) {
                ls.add((toString(entry.getValue())));
            }
            String ts = String.join("\n", ls);
            writer.append(ts);
            writer.append("\n");
            var history = historyToString(historyManager);
            writer.append("\n").append(history);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }

    // Восстановление из файла
    protected void load() {
        long maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine(); // Пропускаем заголовок
            while (true) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                // Задачи
                final Task task = fromString(line);

                // добавить задачу в менеджер
                final Long id = task.getId();
                if (maxId < id) {
                    maxId = id;
                }
                TaskType type = task.getType();
                switch (type) {
                    case TASK:
                        singleTask.put(id, task);
                        break;
                    case SUBTASK:
                        Sub sub = (Sub) task;
                        subTask.put(id, sub);
                        break;
                    case EPIC:
                        Epic epic = (Epic) task;
                        epicTask.put(id, epic);
                        break;
                }
            }
            String line = reader.readLine();
            if (line != null) {
                var histIds = historyFromString(line);
                for (Integer id : histIds) {
                    System.out.println(id);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки");
        }

    }

    public static void loadFromFile(File file) {
        final FileBackedTasksManager manager = new FileBackedTasksManager(file);
        manager.load();
    }

    // Сохранение задачи в строку
    private String toString(Task task) {
        TaskType type = task.getType();
        String ts = task.getId() + "," + type + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription();
        switch (type) {
            case TASK:
                return ts;
            case SUBTASK:
                Sub sub = (Sub) task;
                return ts + "," + sub.getEpicId();
            case EPIC:
                return ts;
            default:
                throw new ManagerSaveException("Задача отсутствует");
        }
    }

    //Создание задачи из строки
    public Task fromString(String value) {
        final String[] values = value.split(",");
        Task res = null;
        long taskId = Integer.parseInt(values[0]);
        var type = TaskType.valueOf(values[1]);
        String name = values[2];
        TaskStatus status = TaskStatus.valueOf(values[3]);
        String description = values[4];
        switch (type) {
            case TASK:
                res = new Task(name, description, status, type);
                res.setId(taskId);
                break;
            case SUBTASK:
                Long epicId = Long.parseLong(values[5]);
                res = new Sub(name, description, status, type, epicId);
                res.setId(taskId);
                break;
            case EPIC:
                res = new Epic(name, description, status, type);
                res.setId(taskId);
                break;
            default:
                throw new ManagerSaveException("Неверено введена задача");
        }
        return res;
    }

    // Сохранение истории в строчку
    public String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        var historyId = manager.getHistory();
        if (historyId.size() != 0) {
            for (Task task : historyId) {
                sb.append(task.getId()).append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        return sb.toString();
    }

    //  Восстановление истории
    public static List<Integer> historyFromString(String value) {
        final String[] id = value.split(",");
        var history = new ArrayList<Integer>();
        for (String i : id) {
//            history.add(Integer.parseInt(id)); Если задач будет больше 128
            history.add(Integer.valueOf(i));
        }
        return history;
    }
}