package Tasks;

public class Sub extends Task {

    protected Long epicId;

    public Sub(String name, String description, TaskStatus status, Long epicId) {
        super(name, description, status);
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


//    @Override
//    public String toString() {
//        String result;
//        result = "Имя сабтаска - " + getName() + "\n"
//                + "ID сабтаска - " + getId() + "\n"
//                + "Описание сабтаска - " + getDescription() + "\n"
//                + "Статус сабтаска - " + getStatus() + "\n"
//                + "EpicId" + getEpicId() + "\n"
//                + "-------------";
//
//        return result;
//    }

}