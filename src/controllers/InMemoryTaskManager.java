package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements Manager {
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int counterId;

    private final HistoryManager historyManager = new InMemoryHistoryManager();


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Integer createTask(Task task) { //создание задачи;
        task.setId(counterId++);
        task.setStatus(TaskStatus.NEW);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public Integer createEpic(Epic epic) { //создание эпика
        epic.setId(counterId++);
        epic.setStatus(TaskStatus.NEW);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        subtask.setId(counterId++);
        subtask.setStatus(TaskStatus.NEW);
        subtasks.put(subtask.getId(), subtask);
        addIdSubtaskToEpics(subtask.getId(), subtask.getIdEpic());
        subtasks.put(subtask.getId(),subtask);
        checkStatusOfSubtask(subtask.getIdEpic());
        return subtask.getId();
    }

    @Override
    public void addIdSubtaskToEpics(int idSubtask, int idEpic) { // добавление id сабтаска в список сабтасков эпика
        Epic epic = epics.get(idEpic); // получаем объект Epic с указанным id
        epic.getIdSubtask().add(idSubtask); // добавляем id сабтаска в ArrayList Epic'а
    }

    @Override
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

    @Override
    public Collection<Task> getTasks() { //Получение списка задач
        return tasks.values();
    }

    @Override
    public Collection<Epic> getEpics() { //Получение списка эпиков
        return epics.values();
    }

    public Collection<Subtask> getSubtasks() { //Получение списка сабтасков
        return subtasks.values();
    }

    @Override
    public void removeAllTasks() { //удаление всех задач
        tasks.clear();
    }

    @Override
    public void removeAllEpics() { //удаление всех эпиков
        epics.clear();
        subtasks.clear();
    }

    @Override
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

    @Override
    public Task getIdTask(int idTask) { //получение задачи по ID
        historyManager.addTask(tasks.get(idTask));
        return tasks.get(idTask);
    }

    @Override
    public Epic getIdEpic(int idEpic) { //получение эпика по ID
        historyManager.addTask(epics.get(idEpic));
        return epics.get(idEpic);
    }

    @Override
    public Subtask getSubtaskById(int idSubtask) { //получение сабтаска по ID
        historyManager.addTask(subtasks.get(idSubtask));
        return subtasks.get(idSubtask);
    }

    @Override
    public void updateTask(Task task) { // Обновление Task
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) { // Обновление Subtask
        Subtask oldSubtask = subtasks.get(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        addIdSubtaskToEpics(subtask.getId(), subtask.getIdEpic()); // добавляем сабтакс в список нового эпика
        removeIdSubtaskFromEpics(subtask.getId(), oldSubtask.getIdEpic()); // удаляем сабтакс из списка старого эпика
        checkStatusOfSubtask(subtask.getIdEpic()); // обновляем статус у нового эпика
        checkStatusOfSubtask(oldSubtask.getIdEpic()); // обновляем статус у старого эпика
    }

    @Override
    public void updateEpic(Epic epic) { // Обновление Epic
        final Epic savedEpic = epics.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        checkStatusOfSubtask(epic.getId()); // обновляем статус у эпика
    }

    @Override
    public void deleteTaskByID(int id) { //удаление задачи по id
        tasks.remove(id);
    }

    @Override
    public void deleteEpicByID(int id) { //удаление эпика по id
        final Epic epic = epics.remove(id); //удаляем эпик из общего списка
        for (Integer subtaskId : epic.getIdSubtask()) { //удаляем все подзадачи этого эпика из общего списка в цикле
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubtaskByID(int id) { //удаление сабтасков по id
        Subtask subtask = subtasks.get(id);
        int idEpic = subtask.getIdEpic();
        subtasks.remove(id);
        checkStatusOfSubtask(idEpic); // обновляем статус у эпика
    }


    @Override
    public void checkStatusOfSubtask(int idEpic) { // проверяем статусы всех сабтасков при обновлении каждого из них и меняем у эпика
        int countNew = 0;
        int countDone = 0;
        int countSubtasksOfEpic = 0;
        Epic epic = epics.get(idEpic); // берем из HashMap Epic с необходимым id
        for (Subtask value : subtasks.values()) {
            if (value.getIdEpic() == idEpic) {
                countSubtasksOfEpic++; // считаем количество сабтасков у определенного эпика
                if (value.getStatus().equals("DONE")) {
                    countDone++;
                } else if (value.getStatus().equals("NEW")) {
                    countNew++;
                }
            }
        }
        // Смена статуса у родительского эпика в зависимости от статусов его сабтасков
        if (countDone == countSubtasksOfEpic) { // DONE
            epic.setStatus(TaskStatus.DONE);
        } else if (countNew == countSubtasksOfEpic) { // NEW
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}