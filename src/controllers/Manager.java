package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.Collection;
import java.util.List;

public interface Manager {

    List<Task> getHistory();

    Integer createTask(Task task); //создание задачи;


    Integer createEpic(Epic epic); //создание эпика


    Integer createSubtask(Subtask subtask);

    void addSubtaskToEpics(Subtask subtask, int idEpic);

    void removeIdSubtaskFromEpics(int idSubtask, int idEpic);

    Collection<Task> getTasks();

    Collection<Epic> getEpics();

    Collection<Subtask> getSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();
    Task getTaskById(int idTask);
    Epic getEpicById(int idEpic);

    Subtask getSubtaskById(int idSubtask);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);
    void updateEpic(Epic epic);

    void deleteTaskByID(Integer id);

    void deleteEpicByID(Integer id);

    void deleteSubtaskByID(Integer id);

    List<Subtask> getAllSubtasksByEpicId(long epicId);

    void checkStatusOfSubtask(Integer idEpic);

    List<Task> getPrioritizedTasks();
}