package Tasks;

import java.time.LocalDateTime;

public class Sub extends Task {

    protected Long epicId;
    public Sub(String name, String description, TaskStatus status, TaskType type, Long epicId,LocalDateTime start, long duration) {
        super(name, description, status,type,start,duration);
        this.epicId = epicId;
    }
    public Sub(String name, String description, TaskStatus status, TaskType type,Long epicId) {
        super(name, description, status,type);
        this.epicId = epicId;
    }

    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Sub{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", id=" + getId() +
                '}';
    }

}