package Tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    List<Sub> subTaskIds = new ArrayList<>();
    public Epic(String name, String description, TaskStatus status, TaskType type, LocalDateTime start, long duration) {
        super(name, description, status, type,start,duration);
    }

    public Epic(String name, String description, TaskStatus status, TaskType type) {
        super(name, description, status, type);
    }

    public List<Sub> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(List<Sub> subTaskIds) {
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