package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> idSubtask;

    public Epic(String name, String description) {
        super(name, description);
        idSubtask = new ArrayList<>();

    }
    public Epic(String name, String description, int id) {
        super(name, description, id);
        idSubtask = new ArrayList<>();
    }

    public ArrayList<Integer> getIdSubtask() {
        return idSubtask;
    }

    public void setIdSubtask(ArrayList<Integer> idSubtask) {
        this.idSubtask = idSubtask;
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
