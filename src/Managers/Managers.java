package Managers;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getHistoryDefault() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBacked() {
        return new FileBackedTasksManager(new File("taskPrinter.csv"));
    }

}
