package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.Collection;
import java.util.HashMap;

public class Manager {
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
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

    public void removeIdSubtaskFromEpics(int idSubtask, int idEpic) {
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
    }
    public Task getIdTask(int idTask) { //получение задачи по ID
        return tasks.get(idTask);
    }

    public Epic getIdEpic(int idEpic) { //получение эпика по ID
        return epics.get(idEpic);
    }

    public void getSubtaskByIdEpic(int idEpic, int idSubtask) { //получение сабтаска по ID
        for (Integer key : subtasks.keySet()) {
            if (key == idSubtask) {
                subtasks.get(key);
                return;
            }
        }
    }

    // 2.5
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
            Epic oldEpic = epics.get(epic.getId());
            epic.setIdSubtask(oldEpic.getIdSubtask()); // сохраняем старый список сабтасков у эпика
            epics.put(epic.getId(), epic);
            checkStatusOfSubtask(epic.getId()); // обновляем статус у  эпика
        }

       public void deleteById(int id) { //2.6 Удаление по ID
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (int i = 0; i < epic.getIdSubtask().size(); i++) { //удаление всех сабтасков эпика
                deleteById(epic.getIdSubtask().get(i));
            }
            epics.remove(id); //удаляем эпик
        } else {
            subtasks.remove(id);
        }
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