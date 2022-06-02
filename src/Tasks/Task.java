package Tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private TaskStatus status;
    private TaskType type;
    private Long id;
    private LocalDateTime start;
    private long duration;
    private LocalDateTime end;

    public Task(String name, String description, TaskStatus status, TaskType type, LocalDateTime start, long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.start = start;
        this.duration = duration;
        this.end = getEnd();
    }

    public Task(String name, String description, TaskStatus status, TaskType type) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getEnd() {
        return getStart().plusMinutes(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(getName(),
                task.getName()) && Objects.equals(getDescription(),
                task.getDescription()) && getStatus() == task.getStatus() && Objects.equals(getId(),
                task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(),
                getDescription(),
                getStatus(),
                getId());
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", startTime=" + start +
                ", duration" + duration +
                '}';
    }

}