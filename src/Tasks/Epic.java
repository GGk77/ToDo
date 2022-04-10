package Tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    List<Long> subTaskIds = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public List<Long> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(List<Long> subTaskIds) {
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

}