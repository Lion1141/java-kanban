package model;

import java.util.ArrayList;
import static model.TaskType.EPIC;

public class Epic extends Task{
    private ArrayList<Integer> idSubtask;

    public Epic(String name, String description) {
        super(name, description);
        idSubtask = new ArrayList<>();
        super.setTaskType(EPIC);

    }
    public Epic(String name, String description, int id) {
        super(name, description, id);
        idSubtask = new ArrayList<>();
        super.setTaskType(EPIC);
    }

    public ArrayList<Integer> getIdSubtask() {
            return idSubtask;
    }

    public void removeIdSubtasks() {
        idSubtask.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", subtasks=" + idSubtask +
                '}';
    }
}
