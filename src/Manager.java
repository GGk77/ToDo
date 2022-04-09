import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Manager {

    Integer generateId = 0;

    HashMap<Integer, Task> singleTask = new HashMap<>();
    HashMap<Integer, Sub> subTask = new HashMap<>();
    HashMap<Integer, Epic> epicTask = new HashMap<>();


    // Получаем список всех задач.
    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(singleTask.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<Epic>(epicTask.values());
    }

    public ArrayList<Sub> getSubs() {
        return new ArrayList<Sub>(subTask.values());
    }

    public Task getTask(Integer id) {
        return singleTask.get(id);
    }

    public Epic getEpic(Integer id) {
        return epicTask.get(id);
    }

    public Sub getSub(Integer id) {
        return subTask.get(id);
    }

    // Создание

    public Integer addTask(Task task) {
        Integer id = idGenerate();
        task.setId(id);
        singleTask.put(id, task);
        return id;
    }

    public Integer addEpic(Epic epic) {
        Integer id = idGenerate();
        epic.setId(id);
        epicTask.put(id, epic);
        updateEpicStatus(id);
        return id;
    }

    public Integer addSubTask(Sub sub) {
        if (!epicTask.containsKey(sub.epicId)) {
            System.out.println("Ошибка");
        }
        Integer id = idGenerate();
        sub.setId(id + 1);
        subTask.put(id, sub);
        Integer epicId = sub.getEpicId();
        Epic epic = epicTask.get(epicId);
        epic.getSubTaskIds().add(id);
        return id;
    }


    // Получение по ID

    public void getTaskById(Integer id) {
        if (singleTask.containsKey(id)) {
            Task ID = singleTask.get(id);
            System.out.println(ID);

        } else if (epicTask.containsKey(id)) {
            Epic ID = epicTask.get(id);
            System.out.println(ID);

        } else if (subTask.containsKey(id)) {
            Sub ID = subTask.get(id);
            System.out.println(ID);

        } else {
            System.out.println("Задачи с таким Id нет");
        }
    }

    // Получение подзадач эпика

    public ArrayList<Sub> getSubtaskEpic(Integer epicId) {
        ArrayList<Integer> subListId = epicTask.get(epicId).getSubTaskIds();
        ArrayList<Sub> subList = new ArrayList<>();
        for (Integer sub : subListId) {
            subList.add(subTask.get(sub));

        }
        return subList;
    }

    // Обновление

    public void updateTask(Task task) {
        Integer id = task.getId();
        if (singleTask.containsKey(id)) {
            singleTask.put(id, task);
        }
    }

    public void updateEpic(Epic epic) {
        Integer id = epic.getId();
        if (epicTask.containsKey(id)) {
            epicTask.put(id, epic);
        }

    }

    public void updateSub(Sub sub) {
        Integer epicId = sub.getEpicId();
        if (Objects.isNull(epicId)) {
            return;
        }
        if (epicTask.containsKey(epicId)) {
            return;
        }
        Integer id = sub.getId();
        if (subTask.containsKey(id)) {
            subTask.put(id, sub);
        }
        updateEpicStatus(sub.getEpicId());
    }

    // Генератор ID

    public Integer idGenerate() {
        return ++generateId ;
    }

    // Удаление

    public void removeAllTasks() {
        singleTask.clear();
        subTask.clear();
        epicTask.clear();

        System.out.println("Ничего нет");
    }

    public void deleteTaskByIdSingle(Integer id) {
        singleTask.remove(id);
    }

    public void deleteTaskByIdEpic(Integer id) {
        if (epicTask.containsKey(id)) {
            ArrayList<Integer> idEpic = epicTask.get(id).getSubTaskIds();
            for (Integer subTaskId : idEpic) {
                subTask.remove(subTaskId);
            }
            epicTask.remove(id);
        }
    }

    public void deleteTaskByIdSubtask(Integer id) {
        subTask.remove(id);

    }

    // Обновление статуса Epic

    protected void updateEpicStatus(Integer epicId) {
        int statusNew = 0;
        int statusDone = 0;
        ArrayList<Integer> list;
        list = epicTask.get(epicId).getSubTaskIds();
        if (list.size() == 0) {
            epicTask.get(epicId).setStatus(TaskStatus.NEW);
        } else {
            for (Integer value : list) {
                TaskStatus status = subTask.get(value).getStatus();
                if (status.equals(TaskStatus.NEW)) {
                    statusNew++;
                } else if (status.equals(TaskStatus.DONE)) {
                    statusDone++;
                }
            }
            if (statusNew == list.size()) {
                epicTask.get(epicId).setStatus(TaskStatus.NEW);
            } else if (statusDone == list.size()) {
                epicTask.get(epicId).setStatus(TaskStatus.DONE);
            } else {
                epicTask.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }
}