package controllers;

import api.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

import static util.Managers.getGsonWithLocalDateTimeAdapter;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private final Gson gson;

    public HttpTaskManager(String url, int port) {
        super(null);
        this.kvTaskClient = new KVTaskClient(url);
        gson = getGsonWithLocalDateTimeAdapter();
    }

    public void load() {
        try {
            String kvTasks = kvTaskClient.load("kvTasks/task");
            if (kvTasks != null) {
                ArrayList<Task> taskList = gson.fromJson(kvTasks, new TypeToken<ArrayList<Task>>() {
                }.getType());
                for (Task task : taskList) {
                    createTask(task);
                }
            }
            String kvEpics = kvTaskClient.load("kvTasks/epic");
            if (kvEpics != null) {
                ArrayList<Epic> epicList = gson.fromJson(kvEpics, new TypeToken<ArrayList<Epic>>() {
                }.getType());
                for (Epic epic : epicList) {
                    createEpic(epic);
                }
            }
            String kvSubtasks = kvTaskClient.load("kvTasks/subtask");
            if (kvSubtasks != null) {
                ArrayList<Subtask> subtaskList = gson.fromJson(kvSubtasks, new TypeToken<ArrayList<Subtask>>() {
                }.getType());
                for (Subtask subtask : subtaskList) {
                    createSubtask(subtask);
                }
            }
            String history = kvTaskClient.load("kvTasks/history");
            if (history != null) {
                ArrayList<Task> historyList = gson.fromJson(history, new TypeToken<List<Task>>() {
                }.getType());
                for (Task task : historyList) {
                    getTaskById(task.getId());
                    getEpicById(task.getId());
                    getSubtaskById(task.getId());
                }
                for (Integer id : tasks.keySet()) {
                    if (id > this.counterId) {
                        this.counterId = id;
                    }
                }
                for (Integer id : epics.keySet()) {
                    if (id > this.counterId) {
                        this.counterId = id;
                    }
                }
                for (Integer id : subtasks.keySet()) {
                    if (id > this.counterId) {
                        this.counterId = id;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при восстановлении менеджера задач.");
        }
    }

    @Override
    public void save() {
        kvTaskClient.put("tasks/task", gson.toJson(tasks));
        kvTaskClient.put("tasks/epic", gson.toJson(epics));
        kvTaskClient.put("tasks/subtask", gson.toJson(subtasks));
        kvTaskClient.put("tasks/history", gson.toJson(historyManager.getHistory()));
    }
}
