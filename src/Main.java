public class Main {
    public static void main(String[] args) {



        Manager manager = new Manager();

        Epic epic1 = new Epic("Epic1", "Сделать проект", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic2", "Помыть машину", TaskStatus.NEW);

        int epic1Id = manager.addEpic(epic1);
        int epic2Id = manager.addEpic(epic2);

        Sub subtask1 = new Sub("Subtask1", "Сделать чертеж", TaskStatus.NEW,epic1Id);
        Sub subtask2 = new Sub("Subtask2", "Распечатать", TaskStatus.NEW,epic1Id);
        Sub subtask3 = new Sub("Subtask3", "Съездить на мойку", TaskStatus.NEW,epic2Id);

        int sub1Id = manager.addSubTask(subtask1);
        int sub2Id = manager.addSubTask(subtask2);
        int sub3Id = manager.addSubTask(subtask3);

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
//        manager.deleteTaskByIdSubtask();


    }

    public static void printAllTask(Manager manager) {

        System.out.println("Epic");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Sub");
        for (Sub sub : manager.getSubs()) {
            System.out.println(sub);
        }

    }

}
