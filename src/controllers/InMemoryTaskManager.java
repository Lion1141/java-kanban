package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import util.Managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements Manager {
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int counterId;

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Integer createTask(Task task) { //создание задачи;
        task.setId(counterId++);
        int taskId = task.getId();
        task.setStatus(TaskStatus.NEW);
        tasks.put(taskId, task);
        return taskId;
    }

    @Override
    public Integer createEpic(Epic epic) { //создание эпика
        epic.setId(counterId++);
        int epicId = epic.getId();
        epic.setStatus(TaskStatus.NEW);
        epics.put(epic.getId(), epic);
        return epicId;
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        subtask.setId(counterId++);
        int subtaskEpicId = subtask.getIdEpic();
        int subtaskId = subtask.getId();
        subtask.setStatus(TaskStatus.NEW);
        addIdSubtaskToEpics(subtaskId, subtaskEpicId);
        subtasks.put(subtaskId,subtask);
        checkStatusOfSubtask(subtaskEpicId);
        return subtaskId;
    }

    @Override
    public void addIdSubtaskToEpics(int idSubtask, int idEpic) { // добавление id сабтаска в список сабтасков эпика
        Epic epic = epics.get(idEpic); // получаем объект Epic с указанным id
        var idSubtasks = epic.getIdSubtask();
        if(idSubtasks.contains(idSubtask)) { //проверка на наличие такого id
            return;
        }
        idSubtasks.add(idSubtask); // добавляем id сабтаска в ArrayList Epic'а
    }

    @Override
    public void removeIdSubtaskFromEpics(int idSubtask, int idEpic) { //удаление id сабтасков из списка эпика
        Epic epic = epics.get(idEpic); // получаем объект Epic с указанным id
         epic.getIdSubtask().remove((Object)idSubtask); //удалён цикл и условие для лучшей читаемости
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
        tasks.values().forEach(task -> historyManager.remove(task.getId()));
        tasks.clear();
    }

    @Override
    public void removeAllEpics() { //удаление всех эпиков
        epics.values().forEach(epic -> historyManager.remove(epic.getId()));
        epics.clear();

        subtasks.values().forEach(subtask -> historyManager.remove(subtask.getId()));
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() { //удаление всех сабтасков
        subtasks.values().forEach(subtask -> historyManager.remove(subtask.getId()));
        subtasks.clear();

        epics.values().forEach(epic -> {
            epic.removeIdSubtasks();
        });

        for (Integer idEpic : epics.keySet()) {
            checkStatusOfSubtask(idEpic);
        }
    }

    @Override
    public Task getTaskById(int idTask) { //получение задачи по ID
        historyManager.addTask(tasks.get(idTask));
        return tasks.get(idTask);
    }

    @Override
    public Epic getEpicById(int idEpic) { //получение эпика по ID
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
        int subtaskEpicId = subtask.getIdEpic();
        int subtaskId = subtask.getId();
        Subtask oldSubtask = subtasks.get(subtaskId);
        int oldSubtaskIdEpic = oldSubtask.getIdEpic();
        subtasks.put(subtaskId, subtask);
        if (subtaskId != oldSubtaskIdEpic) {
            /*исправлен баг из за которого дублировался id сабтаска
            путём проверки на изменение id Эпика
             */
            addIdSubtaskToEpics(subtaskId, subtaskEpicId); // добавляем сабтаск в список нового эпика
            removeIdSubtaskFromEpics(subtaskId, oldSubtaskIdEpic); // удаляем сабтаск из списка старого эпика
        }
        checkStatusOfSubtask(subtaskEpicId); // обновляем статус у нового эпика
        checkStatusOfSubtask(oldSubtaskIdEpic); // обновляем статус у старого эпика
    }

    @Override
    public void updateEpic(Epic epic) { // Обновление Epic
        int epicId = epic.getId();
        final Epic savedEpic = epics.get(epicId);
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        checkStatusOfSubtask(epicId); // обновляем статус у эпика
    }

    @Override
    public void deleteTaskByID(int id) { //удаление задачи по id
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpicByID(int id) { //удаление эпика по id
        final Epic epic = epics.remove(id); //удаляем эпик из общего списка
        historyManager.remove(id);
        for (Integer subtaskId : epic.getIdSubtask()) { //удаляем все подзадачи этого эпика из общего списка в цикле
            historyManager.remove(subtaskId);
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubtaskByID(int id) { //удаление сабтасков по id
        Subtask subtask = subtasks.get(id);
        int idEpic = subtask.getIdEpic();
        historyManager.remove(id);
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
                    break;
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