package model;
import java.time.Duration;
import java.time.LocalDateTime;

import static model.TaskType.SUBTASK;


public class Subtask extends Task {
    private final int idEpic; //ID эпика, к которому относится сабтаск

    public Subtask(String name, String description, int idEpic) { // конструктор при создании объекта (id генерируется первый свободный)
        super(name, description);
        super.setTaskType(SUBTASK);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, int id, int idEpic) {
        super(name, description, id);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, int id, TaskStatus status, int idEpic) {
        super(name, description, id, status);
        this.idEpic = idEpic;
        super.setTaskType(SUBTASK);
    }

    public Subtask(String name, int id, String description, TaskStatus status, TaskType taskType, LocalDateTime startTime, Duration duration, int idEpic) {
        super(name, id, description, status, taskType, startTime, duration);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, LocalDateTime startTime, Duration duration, int idEpic) {
        super(name, description, startTime, duration);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, int id, TaskStatus taskStatus, LocalDateTime taskStartTime, Duration taskDuration, int idEpic) {
        super(name, description, id, taskStatus, taskStartTime, taskDuration);
        this.idEpic = idEpic;
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