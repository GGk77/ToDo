package Managers;

import Http.HttpTaskManager;
import Http.KVServer;
import Managers.FileBacked.FileBackedTasksManager;
import Managers.History.HistoryManager;
import Managers.History.InMemoryHistoryManager;
import Managers.TaskManager.InMemoryTaskManager;
import Managers.TaskManager.TaskManager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new HttpTaskManager(KVServer.PORT);
    }

    public static HistoryManager getHistoryDefault() {
        return new InMemoryHistoryManager();
    }

/*    public static FileBackedTasksManager getFileBacked() {
        return new FileBackedTasksManager(new File("taskPrinter.csv"));
    }*/

}
