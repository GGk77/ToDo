public class Task {

    String name;
    String description;
    TaskStatus status;
    int id;


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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String result;
        result = "Имя таска - " + getName() + "\n"
                + "ID таска - " + getId() + "\n"
                + "Описание таска - " + getDescription() + "\n"
                + "Статус таска - " + getStatus() + "\n"
                + "-------------";
        return result;
    }
}