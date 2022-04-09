import java.util.ArrayList;

public class Epic extends Task{


    ArrayList<Integer> subTaskIds;
    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        subTaskIds = new ArrayList<>();

    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }


    @Override
    public String toString() {
        String result;
        result = "Имя эпика - " + getName() + "\n"
                + "ID Эпика - " + getId() + "\n"
                + "Описание Эпика - " + getDescription() + "\n"
                + "Статус Эпика - " + getStatus() + "\n"
                + "-------------";
        return result;
    }
}