package Managers;

import Tasks.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
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
//
//       // FileBackedTasksManager.loadFromFile(new File("taskPrinter.csv"));
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
}

