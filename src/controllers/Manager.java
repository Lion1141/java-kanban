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

    void addIdSubtaskToEpics(int idSubtask, int idEpic);

    void removeIdSubtaskFromEpics(int idSubtask, int idEpic);

    Collection<Task> getTasks();

    Collection<Epic> getEpics();

    Collection<Subtask> getSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();
    Task getIdTask(int idTask);
    Epic getIdEpic(int idEpic);

    Subtask getSubtaskById(int idSubtask);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);
    void updateEpic(Epic epic);

    void deleteTaskByID(int id);

    void deleteEpicByID(int id);

    void deleteSubtaskByID(int id);


    void checkStatusOfSubtask(int idEpic);
}