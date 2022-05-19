package Managers;

import Tasks.*;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        TaskManager manager1 = Managers.getFileBacked();

        //Создание задач
        Long firstTask = manager1.addTask(new Task("Наименование задачи 1", "Описание 1",
                TaskStatus.IN_PROGRESS, TaskType.TASK));
        Long secondTask = manager1.addTask(new Task("Наименование задачи 2", "Описание 2",
                TaskStatus.NEW, TaskType.TASK));
        Long thirdTask = manager1.addTask(new Task("Наименование задачи 3", "Описание 3",
                TaskStatus.DONE, TaskType.TASK));
        Long forthTask = manager1.addTask(new Task("Наименование задачи 4", "Описание 4",
                TaskStatus.IN_PROGRESS, TaskType.TASK));
        Long fifthTask = manager1.addTask(new Task("Наименование задачи 5", "Описание 5",
                TaskStatus.NEW, TaskType.TASK));

        //Создание эпиков
        Long firstEpic = manager1.addEpic(new Epic("Наименование эпика 1", "Описание 1", TaskStatus.NEW, TaskType.EPIC));
        Long secondEpic = manager1.addEpic(new Epic("Наименование эпика 2", "Описание 2", TaskStatus.NEW, TaskType.EPIC));
        Long thirdEpic = manager1.addEpic(new Epic("Наименование эпика 3", "Описание 3", TaskStatus.NEW, TaskType.EPIC));
        Long forthEpic = manager1.addEpic(new Epic("Наименование эпика 4", "Описание 4", TaskStatus.NEW, TaskType.EPIC));

        //Создание подзадачи к эпику
        Long firstSubtaskToFirstEpic = manager1.addSubTask(new Sub("Подзадача 1 к эпику 1",
                "Описание 1", TaskStatus.DONE, TaskType.SUBTASK, firstEpic));
        Long secondSubtaskToFirstEpic = manager1.addSubTask(new Sub("Подзадача 2 к эпику 1",
                "Описание 2", TaskStatus.DONE, TaskType.SUBTASK, firstEpic));

        //Вызов некоторых задач, эпиков и подзадач
        //Задачи
        manager1.getTaskById(firstTask);
        manager1.getTaskById(thirdTask);
        manager1.getTaskById(fifthTask);

        //Эпики
        manager1.getEpic(firstEpic);
        manager1.getEpic(forthEpic);

        //Подзадача
        manager1.getSub(firstSubtaskToFirstEpic);

        System.out.println(manager.getHistory());

        FileBackedTasksManager.loadFromFile(new File("taskPrint.csv"));
    }
}


//        TaskManager manager = Managers.getDefault();
//        TaskManager manager1 = new FileBackedTasksManager(new File("task.csv"),true);
//        // TODO manager==manager1
//        Epic epic1 = new Epic("Epic1", "Сделать проект", TaskStatus.NEW, TaskType.EPIC);
//        Epic epic2 = new Epic("Epic2", "Помыть машину", TaskStatus.NEW, TaskType.EPIC);
//
//        Long epic1Id = manager.addEpic(epic1);
//        Long epic2Id = manager.addEpic(epic2);
//
//        Sub subtask1 = new Sub("Subtask1", "Сделать чертеж", TaskStatus.NEW,TaskType.SUBTASK, epic1Id);
//        Sub subtask2 = new Sub("Subtask2", "Распечатать", TaskStatus.NEW, TaskType.SUBTASK, epic1Id);
//        Sub subtask3 = new Sub("Subtask3", "Подписать", TaskStatus.NEW, TaskType.SUBTASK, epic1Id);
//
//        Long sub1Id = manager.addSubTask(subtask1);
//        Long sub2Id = manager.addSubTask(subtask2);
//        Long sub3Id = manager.addSubTask(subtask3);
//
//        Sub sub1 = manager.getSub(sub1Id);
//        Epic epicOne = manager.getEpic(epic1Id);
//        Sub sub2 = manager.getSub(sub2Id);
//        Epic epicTwo = manager.getEpic(epic2Id);
//        Sub sub3 = manager.getSub(sub3Id);
//        Epic epicOneAgain = manager.getEpic(epic1Id);
//        Sub sub3Again = manager.getSub(sub3Id);

//        printAllTask(manager);
//        sub1.setStatus(TaskStatus.IN_PROGRESS);
//        sub2.setStatus(TaskStatus.IN_PROGRESS);
//        sub3.setStatus(TaskStatus.DONE);
//        manager.updateEpicStatus(1);
//        manager.updateEpicStatus(2);

//        manager.deleteTaskByIdEpic(1);
//        manager.deleteTaskByIdSubtask(4);
//
//        printAllTask(manager);
//    }

//    public static void printAllTask(TaskManager manager) {
//
//        System.out.println("Epic");
//        for (Epic epic : manager.getEpics()) {
//            System.out.println(epic);
//        }
//        System.out.println("Sub");
//        for (Sub sub : manager.getSubs()) {
//            System.out.println(sub);
//        }
//        System.out.println("History");
//        for (Task task :manager.getHistory()) {
//            System.out.println(task);
//        }
//    }
//}
