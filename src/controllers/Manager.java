package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Manager {
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int counterId;

    public Integer createTask(Task task) { //создание задачи;
        task.setId(counterId++);
        task.setStatus("NEW");
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public Integer createEpic(Epic epic) { //создание эпика
        epic.setId(counterId++);
        epic.setStatus("NEW");
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public Integer createSubtask(Subtask subtask) {
        subtask.setId(counterId++);
        subtask.setStatus("NEW");
        subtasks.put(subtask.getId(), subtask);
        addIdSubtaskToEpics(subtask.getId(), subtask.getIdEpic());
        subtasks.put(subtask.getId(),subtask);
        checkStatusOfSubtask(subtask.getIdEpic());
        return subtask.getId();
    }

    private void addIdSubtaskToEpics(int idSubtask, int idEpic) { // добавление id сабтаска в список сабтасков эпика
        Epic epic = epics.get(idEpic); // получаем объект Epic с указанным id
        epic.getIdSubtask().add(idSubtask); // добавляем id сабтаска в ArrayList Epic'а
    }

    public void removeIdSubtaskFromEpics(int idSubtask, int idEpic) { //удаление id сабтасков из списка эпика
        Epic epic = epics.get(idEpic); // получаем объект Epic с указанным id
        int position = 0;
        for (Integer valueId : epic.getIdSubtask()) {
            if (valueId == idSubtask) {
                break;
            }
            position++;
        }
        epic.getIdSubtask().remove(position);
    }

    public Collection<Task> getTasks() { //Получение списка задач
        return tasks.values();
    }

    public Collection<Epic> getEpics() { //Получение списка эпиков
        return epics.values();
    }

    public Collection<Subtask> getSubtasks() { //Получение списка сабтасков
        return subtasks.values();
    }

    public void removeAllTasks() { //удаление всех задач
        tasks.clear();
    }

    public void removeAllEpics() { //удаление всех эпиков
        epics.clear();
        subtasks.clear();
    }

    public void removeAllSubtasks() { //удаление всех сабтасков
        subtasks.clear();
        for (Integer idEpic : epics.keySet()) {
            Epic epic = epics.get(idEpic);
            epic.removeIdSubtasks();
        }
        for (Integer idEpic : epics.keySet()) {
            checkStatusOfSubtask(idEpic);
        }
    }
    public Task getIdTask(int idTask) { //получение задачи по ID
        return tasks.get(idTask);
    }

    public Epic getIdEpic(int idEpic) { //получение эпика по ID
        return epics.get(idEpic);
    }

    public Subtask getSubtaskById(int idSubtask) { //получение сабтаска по ID
        return subtasks.get(idSubtask);
    }

    public void updateTask(Task task) { // Обновление Task
            tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) { // Обновление Subtask
            Subtask oldSubtask = subtasks.get(subtask.getId());
            subtasks.put(subtask.getId(), subtask);
            addIdSubtaskToEpics(subtask.getId(), subtask.getIdEpic()); // добавляем сабтакс в список нового эпика
            removeIdSubtaskFromEpics(subtask.getId(), oldSubtask.getIdEpic()); // удаляем сабтакс из списка старого эпика
            checkStatusOfSubtask(subtask.getIdEpic()); // обновляем статус у нового эпика
            checkStatusOfSubtask(oldSubtask.getIdEpic()); // обновляем статус у старого эпика
    }

    public void updateEpic(Epic epic) { // Обновление Epic
        final Epic savedEpic = epics.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
            checkStatusOfSubtask(epic.getId()); // обновляем статус у эпика
        }

    public void deleteTaskByID(int id) { //удаление задачи по id
        tasks.remove(id);
    }

    public void deleteEpicByID(int id) { //удаление эпика по id
            final Epic epic = epics.remove(id); //удаляем эпик из общего списка
            for (Integer subtaskId : epic.getIdSubtask()) { //удаляем все подзадачи этого эпика из общего списка в цикле
                subtasks.remove(subtaskId);
            }
        }

    public void deleteSubtaskByID(int id) { //удаление сабтасков по id
        Subtask subtask = subtasks.get(id);
        int idEpic = subtask.getIdEpic();
        subtasks.remove(id);
        checkStatusOfSubtask(idEpic); // обновляем статус у эпика
    }


    private void checkStatusOfSubtask(int idEpic) { // проверяем статусы всех сабтасков при обновлении каждого из них и меняем у эпика
        int countNew = 0;
        int countDone = 0;
        int countSubtasksOfEpic = 0;
        Epic epic = epics.get(idEpic); // берем из HashMap Epic с необходимым id
        for (Subtask value : subtasks.values()) {
            if (value.getIdEpic() == idEpic) {
                countSubtasksOfEpic++; // считаем количество сабтасков у определенного эпика
                if (value.getStatus().equalsIgnoreCase("DONE")) {
                    countDone++;
                } else if (value.getStatus().equalsIgnoreCase("NEW")) {
                    countNew++;
                }
            }
        }
        // Смена статуса у родительского эпика в зависимости от статусов его сабтасков
        if (countDone == countSubtasksOfEpic) { // DONE
            epic.setStatus("DONE");
        } else if (countNew == countSubtasksOfEpic) { // NEW
            epic.setStatus("NEW");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }
}