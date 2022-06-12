package Http;

import Exceptions.ManagerSaveException;
import Managers.FileBacked.FileBackedTasksManager;
import Tasks.Epic;
import Tasks.Sub;
import Tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class HttpTaskManager extends FileBackedTasksManager {
    private final KVClient kvClient;
    private final Gson gson = new Gson();

    public HttpTaskManager(int port) {
        super();
        this.kvClient = new KVClient(port);
        load();
    }

    protected void load() {
        try {
            String taskJson = kvClient.load("tasks/task/");
            singleTask = gson.fromJson(taskJson, new TypeToken<HashMap<Integer, Task>>() {
            }.getType());

            String epicJson = kvClient.load("tasks/epic/");
            epicTask = gson.fromJson(epicJson, new TypeToken<HashMap<Integer, Epic>>() {
            }.getType());
            String subJson = kvClient.load("tasks/sub/");

            subTask = gson.fromJson(subJson, new TypeToken<HashMap<Integer, Sub>>() {
            }.getType());

            String historyJson = kvClient.load("tasks/history/");
            historyManager = gson.fromJson(historyJson, new TypeToken<List<Integer>>() {
            }.getType());
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка загрузки");
        }

    }

    @Override
    protected void save() {
        try {
            kvClient.put("tasks/task", gson.toJson(singleTask));
            kvClient.put("tasks/epic", gson.toJson(epicTask));
            kvClient.put("tasks/subs", gson.toJson(subTask));
            kvClient.put("tasks/history", gson.toJson(historyManager));
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка загрузки");
        }
    }
}
