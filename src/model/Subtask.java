package model;
import static model.TaskType.SUBTASK;


public class Subtask extends Task {
    private final int idEpic; //ID эпика, к которому относится сабтаск

    public Subtask(String name, String description, int idEpic) { // конструктор при создании объекта (id генерируется первый свободный)
        super(name, description);
        super.setTaskType(SUBTASK);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, int id, TaskStatus status, int idEpic) {
        super(name, description, id, status);
        this.idEpic = idEpic;
        super.setTaskType(SUBTASK);
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}