public class Sub extends Task {


    protected int epicId;

    public Sub(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }
    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }


    @Override
    public String toString() {
        String result;
        result = "Имя сабтаска - " + getName() + "\n"
                + "ID сабтаска - " + getId() + "\n"
                + "Описание сабтаска - " + getDescription() + "\n"
                + "Статус сабтаска - " + getStatus() + "\n"
                + "EpicId" + getEpicId() + "\n"
                + "-------------";

        return result;
    }

}