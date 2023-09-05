package model;

import java.util.Collection;
import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public HashMap<Integer, Subtask> getSubtasks() { //сохранение сабтаска в хэшмап
        return subtasks;
    }

    public boolean checkStatusSubtasks(String status) { //проверка статуса сабтаска
        for (Subtask subtask : subtasks.values()) {
            if (!subtask.getStatus().equals(status)) {
                return false;
            }
        }
        return true;
    }

    public Collection<Subtask> getValuesSubtasks() { //получаем значение сабтаска
        return subtasks.values();
    }

    public Subtask getIdSubtaskById(int idSubtask) { //получение id сабтаска
        return subtasks.get(idSubtask);
    }

    public void removeAllSubtasks() { //удаляем все сабтаски
        subtasks.clear();
    }

    public void removeIdSubtask(int idSubtask) { //удаляем сабтаски по ID
        subtasks.remove(idSubtask);
    }

    public void updateSubtask(Subtask subtask) { //обновляем сабтаск
        subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subtasks=" + subtasks +
                '}';
    }
}