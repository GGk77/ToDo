package Managers;

import Tasks.Epic;
import Tasks.Sub;
import Tasks.Task;
import Tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Epic epic1 = new Epic("Epic1", "Сделать проект", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic2", "Помыть машину", TaskStatus.NEW);

        Long epic1Id = manager.addEpic(epic1);
        Long epic2Id = manager.addEpic(epic2);

        Sub subtask1 = new Sub("Subtask1", "Сделать чертеж", TaskStatus.NEW, epic1Id);
        Sub subtask2 = new Sub("Subtask2", "Распечатать", TaskStatus.NEW, epic1Id);
        Sub subtask3 = new Sub("Subtask3", "Съездить на мойку", TaskStatus.NEW, epic2Id);

        Long sub1Id = manager.addSubTask(subtask1);
        Long sub2Id = manager.addSubTask(subtask2);
        Long sub3Id = manager.addSubTask(subtask3);

        printAllTask(manager);

        Sub sub1 = manager.getSub(sub1Id);
        sub1.setStatus(TaskStatus.IN_PROGRESS);
        Sub sub2 = manager.getSub(sub2Id);
        sub2.setStatus(TaskStatus.IN_PROGRESS);
        Sub sub3 = manager.getSub(sub3Id);
        sub3.setStatus(TaskStatus.DONE);

        manager.updateEpicStatus(1);
        manager.updateEpicStatus(2);

        printAllTask(manager);

        manager.deleteTaskByIdEpic(2);
        manager.deleteTaskByIdSubtask(4);
        printAllTask(manager);


    }

    public static void printAllTask(TaskManager manager) {

        System.out.println("Epic");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Sub");
        for (Sub sub : manager.getSubs()) {
            System.out.println(sub);
        }
        System.out.println("History");
        for (Task task :manager.getHistory()) {
            System.out.println(task);
        }
    }
}
