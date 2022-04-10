package Tasks;

import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private TaskStatus status;
    private Long id;

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                '}';
    }


//    @Override
//    public String toString() {
//        String result;
//        result = "Имя таска - " + getName() + "\n"
//                + "ID таска - " + getId() + "\n"
//                + "Описание таска - " + getDescription() + "\n"
//                + "Статус таска - " + getStatus() + "\n"
//                + "-------------";
//        return result;
//    }
}