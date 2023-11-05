package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import util.Managers;

import java.util.*;

public class InMemoryTaskManager implements Manager {
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected int counterId;

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
    protected HistoryManager getHistoryManager() {
        return historyManager;
    }

    private final Comparator<Task> comparator = Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder()));
    protected final Set<Task> prioritizedTasks = new TreeSet<>(comparator);

    @Override
    public Integer createTask(Task task) { //создание задачи;
        task.setId(counterId++);
        int taskId = task.getId();
        task.setStatus(TaskStatus.NEW);
        tasks.put(taskId, task);
        prioritizedTasks.add(task);
        checkIntersection(task);
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
        addSubtaskToEpics(subtask, subtaskEpicId);
        subtasks.put(subtaskId,subtask);
        prioritizedTasks.add(subtask);
        checkStatusOfSubtask(subtaskEpicId);
        checkIntersection(subtask);
        return subtaskId;
    }

    @Override
    public void addSubtaskToEpics(Subtask subtask, int idEpic) { // добавление сабтаска в список сабтасков эпика
        Epic epic = epics.get(idEpic); // получаем объект Epic с указанным id
        var idSubtasks = epic.getSubtaskslist();
        if(idSubtasks.contains(subtask)) { //проверка на наличие такого сабтаска
            return;
        }
        epic.addSubtask(subtask); // добавляем сабтаск в ArrayList Epic'а
    }

    @Override
    public void removeIdSubtaskFromEpics(int idSubtask, int idEpic) { //удаление id сабтасков из списка эпика
        Epic epic = epics.get(idEpic); // получаем объект Epic с указанным id
         epic.getSubtaskslist().remove((Object)idSubtask); //удалён цикл и условие для лучшей читаемости
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
        tasks.values().forEach(prioritizedTasks::remove);
        tasks.clear();
    }

    @Override
    public void removeAllEpics() { //удаление всех эпиков
        epics.values().forEach(epic -> historyManager.remove(epic.getId()));
        epics.clear();
        subtasks.values().forEach(prioritizedTasks::remove);
        subtasks.values().forEach(subtask -> historyManager.remove(subtask.getId()));
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() { //удаление всех сабтасков
        subtasks.values().forEach(subtask -> historyManager.remove(subtask.getId()));
        subtasks.clear();
        subtasks.values().forEach(prioritizedTasks::remove);
        epics.values().forEach(epic -> epic.removeIdSubtasks());

        for (Integer idEpic : epics.keySet()) {
            checkStatusOfSubtask(idEpic);
        }
    }

    @Override
    public Task getTaskById(int idTask) { //получение задачи по ID
        if (tasks.containsKey(idTask)) {
            historyManager.addTask(tasks.get(idTask));
            return tasks.get(idTask);
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpicById(int idEpic) { //получение эпика по ID
        if (epics.containsKey(idEpic)) {
            historyManager.addTask(epics.get(idEpic));
            return epics.get(idEpic);
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubtaskById(int idSubtask) { //получение сабтаска по ID
        if (subtasks.containsKey(idSubtask)) {
            historyManager.addTask(subtasks.get(idSubtask));
            return subtasks.get(idSubtask);
        } else {
            return null;
        }
    }

    @Override
    public void updateTask(Task task) { // Обновление Task
        if (task != null) {
            tasks.put(task.getId(), task);
            checkIntersection(task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) { // Обновление Subtask
        if (subtask != null) {
            int subtaskEpicId = subtask.getIdEpic();
            int subtaskId = subtask.getId();
            Subtask oldSubtask = subtasks.get(subtaskId);
            int oldSubtaskIdEpic = oldSubtask.getIdEpic();
            subtasks.put(subtaskId, subtask);
            if (subtaskId != oldSubtaskIdEpic) {
                addSubtaskToEpics(subtask, subtaskEpicId); // добавляем сабтаск в список нового эпика
                removeIdSubtaskFromEpics(subtaskId, oldSubtaskIdEpic); // удаляем сабтаск из списка старого эпика
            }
            checkStatusOfSubtask(subtaskEpicId); // обновляем статус у нового эпика
            checkStatusOfSubtask(oldSubtaskIdEpic); // обновляем статус у старого эпика
            checkIntersection(subtask);
        }
    }

    @Override
    public void updateEpic(Epic epic) { // Обновление Epic
        if (epic != null) {
            int epicId = epic.getId();
            final Epic savedEpic = epics.get(epicId);
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
            checkStatusOfSubtask(epicId); // обновляем статус у эпика
        }
    }

    @Override
    public void deleteTaskByID(Integer id) { //удаление задачи по id
        prioritizedTasks.remove(tasks.get(id));
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpicByID(Integer id) { //удаление эпика по id
        final Epic epic = epics.remove(id); //удаляем эпик из общего списка
        if (epic != null) {
            historyManager.remove(id);
            for (Subtask subtask : epic.getSubtaskslist()) { //удаляем все подзадачи этого эпика из общего списка в цикле
                historyManager.remove(subtask.getId());
                subtasks.remove(subtask.getId());
            }
        }
    }


    @Override
    public void deleteSubtaskByID(Integer id) { //удаление сабтасков по id
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            int idEpic = subtask.getIdEpic();
            prioritizedTasks.remove(subtasks.get(id));
            historyManager.remove(id);
            subtasks.remove(id);
            checkStatusOfSubtask(idEpic); // обновляем статус у эпика
        }
    }


    @Override
    public void checkStatusOfSubtask(Integer idEpic) { // проверяем статусы всех сабтасков при обновлении каждого из них и меняем у эпика
        int countNew = 0;
        int countDone = 0;
        int countSubtasksOfEpic = 0;
        Epic epic = epics.get(idEpic); // берем из HashMap Epic с необходимым id
        for (Subtask value : subtasks.values()) {
            if (value.getIdEpic() == idEpic) {
                countSubtasksOfEpic++; // считаем количество сабтасков у определенного эпика
                if (value.getStatus() == TaskStatus.DONE) {
                    countDone++;
                } else if (value.getStatus() == TaskStatus.NEW) {
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

    @Override
    public List<Task> getPrioritizedTasks() {
        return new LinkedList<>(prioritizedTasks);
    }

    public void checkIntersection(Task task) {
        boolean isIntersection = getPrioritizedTasks().stream().anyMatch(t -> t.getStartTime() != null
                && t.getId() != task.getId()
                && (task.getStartTime().isAfter(t.getStartTime()) && task.getStartTime().isBefore(t.getEndTime())
                || task.getEndTime().isAfter(t.getStartTime()) && task.getEndTime().isBefore(t.getEndTime())
                || task.getStartTime().equals(t.getStartTime())
                || task.getStartTime().equals(t.getEndTime())
                || task.getEndTime().equals(t.getStartTime())
                || task.getEndTime().equals(t.getEndTime())));

        if (isIntersection) {
            throw new IllegalStateException("У задачи есть пересечения");
        }
    }
}