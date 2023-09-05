package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.Collection;
import java.util.HashMap;

public class Manager {
    private int counterId;
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();

    public Task createTask(String name, String description) { //создание задачи
        Task task = new Task();
        task.setId(counterId++);
        task.setName(name);
        task.setDescription(description);
        task.setStatus(Task.NEW_STATUS);

        tasks.put(task.getId(), task);

        return task;
    }

    public Epic createEpic(String name, String description) { //создание эпика
        Epic epic = new Epic();
        epic.setId(counterId++);
        epic.setName(name);
        epic.setDescription(description);
        epic.setStatus(Task.NEW_STATUS);

        epics.put(epic.getId(), epic);

        return epic;
    }

    public Subtask createSubtask(int idEpic, String name, String description) { //создание сабтаска

        Epic epic = epics.get(idEpic);
        HashMap<Integer, Subtask> subtasks = epic.getSubtasks(); //сохранение сабтаска определённого эпика

        Subtask subtask = new Subtask();
        subtask.setId(counterId++);
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setStatus(Task.NEW_STATUS);

        subtasks.put(subtask.getId(), subtask);

        return subtask;
    }

    public void changeSubtaskStatus(int idEpic, int idSubtask, String newStatus) { //изменение статуса сабтаска
        Epic epic = epics.get(idEpic);
        HashMap<Integer, Subtask> subtasks = epic.getSubtasks();
        Subtask subtask = subtasks.get(idSubtask);
        subtask.setStatus(newStatus);

        switch (newStatus) { //проверка статуса сабтаски и присвоение статуса эпику в зависимости от результата проверки
            case Task.IN_PROGRESS_STATUS:
                if (!epic.getStatus().equals(Task.IN_PROGRESS_STATUS)) {
                    epic.setStatus(Task.IN_PROGRESS_STATUS);
                }
                break;
            case Task.DONE_STATUS:
                if (epic.checkStatusSubtasks(Task.DONE_STATUS)) {
                    epic.setStatus(Task.DONE_STATUS);
                } else {
                    epic.setStatus(Task.IN_PROGRESS_STATUS);
                }
        }
    }

    //Для удобства работы в будущем и упрощения чтения я решил реализовать интерфейс коллекций
    public Collection<Task> getTasks() { //Получение списка задач
        return tasks.values();
    }

    public Collection<Epic> getEpics() { //Получение списка эпиков
        return epics.values();
    }

    public Collection<Subtask> getSubtasks(int idEpic) { //Получение списка сабтасков
        Epic epic = epics.get(idEpic);
        return epic.getValuesSubtasks();
    }

    public Task getIdTask(int idTask) { //получение задачи по ID
        return tasks.get(idTask);
    }

    public Epic getIdEpic(int idEpic) { //получение эпика по ID
        return epics.get(idEpic);
    }

    public Subtask getSubtaskByIdEpic(int idEpic, int idSubtask) { //получение сабтаска по ID
        Epic epic = epics.get(idEpic);
        return epic.getIdSubtaskById(idSubtask);
    }

    public void removeAllTasks() { //удаление всех задач
        tasks.clear();
    }

    public void removeAllEpics() { //удаление всех эпиков
        epics.clear();
    }

    public void removeAllEpicSubtasks(int idEpic) { //удаление всех сабтасков определённого эпика
        Epic epic = epics.get(idEpic);
        epic.removeAllSubtasks();
    }

    public void removeIdTask(int idTask) { //удаление задачи по ID
        tasks.remove(idTask);
    }

    public void removeIdEpic(int idEpic) { //удаление эпика по ID
        epics.remove(idEpic);
    }

    public void removeIdEpicSubtask(int idEpic, int idSubtask) { //удаление сабтаска по ID
        Epic epic = epics.get(idEpic);
        epic.removeIdSubtask(idSubtask);
    }

    public void updateTask(Task task) { //обновление задачи
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) { //обновление эпика
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(int idEpic, Subtask subtask) { //обновление подзадачи
        Epic epic = epics.get(idEpic);
        epic.updateSubtask(subtask);
    }
}