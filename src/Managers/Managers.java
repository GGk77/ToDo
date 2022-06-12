package Managers;

import Http.HttpTaskManager;
import Http.KVServer;
import Managers.History.HistoryManager;
import Managers.History.InMemoryHistoryManager;
import Managers.TaskManager.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new HttpTaskManager(KVServer.PORT);
    }

    public static HistoryManager getHistoryDefault() {
        return new InMemoryHistoryManager();
    }

}
