import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<Long> subTaskIds = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public ArrayList<Long> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(ArrayList<Long> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", id=" + getId() +
                '}';
    }

//    @Override
//    public String toString() {
//        String result;
//        result = "Имя эпика - " + getName() + "\n"
//                + "ID Эпика - " + getId() + "\n"
//                + "Описание Эпика - " + getDescription() + "\n"
//                + "Статус Эпика - " + getStatus() + "\n"
//                + "-------------";
//        return result;
//    }
}