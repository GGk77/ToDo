package Http;

import Managers.FileBacked.FileBackedTasksManager;
import com.google.gson.Gson;
import java.io.IOException;


public class HttpTaskManager extends FileBackedTasksManager {
    private final KVClient kvClient;
    private final Gson gson = new Gson();

    public HttpTaskManager(int port) {
        super();
        this.kvClient = new KVClient(port);
        //        load();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void save() {
        super.save();
        try {
            kvClient.put("tasks/task", gson.toJson(singleTask));
            kvClient.put("tasks/epic", gson.toJson(epicTask));
            kvClient.put("tasks/subs", gson.toJson(subTask));
            kvClient.put("tasks/history", gson.toJson(historyManager));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
