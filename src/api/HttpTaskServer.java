package api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controllers.Manager;
import model.Epic;
import model.Subtask;
import model.Task;
import util.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static api.Endpoints.*;
import static util.Managers.getGsonWithLocalDateTimeAdapter;

public class HttpTaskServer {
    private HttpServer httpServer;
    public void start() throws IOException {
        int DEFAULT_PORT = 8080;

    httpServer = HttpServer.create(new InetSocketAddress(DEFAULT_PORT), 0);

        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + DEFAULT_PORT + " порту!");

}
    public void stop() {
        httpServer.stop(0);
    }
}

class TasksHandler implements HttpHandler {
    private static Gson gson;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public final Manager manager = Managers.getDefault("http://localhost", 8080);

    public TasksHandler() {
        gson = getGsonWithLocalDateTimeAdapter();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();
        String method = exchange.getRequestMethod();

        Endpoints endpoint = getEndpoint(path, method, query);

        switch (endpoint) {
            case GET_TASKS:
                getAllTasks(exchange);
                break;
            case GET_EPICS:
                getAllEpics(exchange);
                break;
            case GET_SUBTASKS:
                getAllSubtasks(exchange);
                break;
            case GET_TASK_BY_ID:
                getTaskById(exchange);
                break;
            case GET_EPIC_BY_ID:
                getEpicById(exchange);
                break;
            case GET_SUBTASK_BY_ID:
                getSubtaskById(exchange);
                break;
            case GET_ALL_SUBTASKS_BY_EPIC_ID:
                getAllSubtasksByEpicId(exchange);
                break;
            case POST_ADD_TASK:
                createTask(exchange);
                break;
            case POST_ADD_EPIC:
                createEpic(exchange);
                break;
            case POST_ADD_SUBTASK:
                createSubtask(exchange);
                break;
            case POST_UPDATE_TASK:
                updateTask(exchange);
                break;
            case POST_UPDATE_EPIC:
                updateEpic(exchange);
                break;
            case POST_UPDATE_SUBTASK:
                updateSubtask(exchange);
                break;
            case DELETE_TASKS:
                removeAllTasks(exchange);
                break;
            case DELETE_EPICS:
                removeAllEpics(exchange);
                break;
            case DELETE_SUBTASKS:
                removeAllSubtasks(exchange);
                break;
            case DELETE_TASK_BY_ID:
                deleteTaskById(exchange);
                break;
            case DELETE_EPIC_BY_ID:
                deleteEpicById(exchange);
                break;
            case DELETE_SUBTASK_BY_ID:
                deleteSubtaskById(exchange);
                break;
            case GET_PRIOR_TASKS:
                getPriorTasks(exchange);
                break;
            case GET_HISTORY:
                getHistory(exchange);
                break;
            case UNKNOWN:
                writeResponse(exchange, "Эндпоинт " + path + " не существует", 404);
        }
    }

    private Endpoints getEndpoint(String path, String method, String query) throws IOException {
        final String task = "task";
        final String epic = "epic";
        final String subtask = "subtask";
        final String history = "history";
        String[] pathArray = path.split("/");
        String[] pathParams = query == null ? null : query.split("&");
        String taskType = "";

        if (pathArray.length > 2) {
            taskType = pathArray[2];
        }
        switch (method) {
            case "GET":
                switch (taskType) {
                    case task:
                        return pathParams == null ? GET_TASKS : GET_TASK_BY_ID;
                    case epic:
                        return pathParams == null ? GET_EPICS : GET_EPIC_BY_ID;
                    case subtask:
                        return pathParams == null ? GET_SUBTASKS : GET_SUBTASK_BY_ID;
                    case history:
                        return GET_HISTORY;
                    case "":
                        return GET_PRIOR_TASKS;
                }
            case "POST":
                switch (taskType) {
                    case task:
                        return pathParams == null ? POST_ADD_TASK : POST_UPDATE_TASK;
                    case epic:
                        return pathParams == null ? POST_ADD_EPIC : POST_UPDATE_EPIC;
                    case subtask:
                        return pathParams == null ? POST_ADD_SUBTASK : POST_UPDATE_SUBTASK;
                }
                    case "DELETE":
                        switch (taskType) {
                            case task:
                                return pathParams == null ? DELETE_TASKS : DELETE_TASK_BY_ID;
                            case epic:
                                return pathParams == null ? DELETE_EPICS : DELETE_EPIC_BY_ID;
                            case subtask:
                                return pathParams == null ? DELETE_SUBTASKS : DELETE_SUBTASK_BY_ID;
                        }
                }
        return UNKNOWN;
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if (responseString.isEmpty()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(bytes);
            }
        }
        exchange.close();
    }

    private String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes());
    }

    private int getIdFromURI(HttpExchange exchange) {
        String[] queryParameters = exchange.getRequestURI().getQuery().split("&");
        String[] idParameter = queryParameters[0].split("=");

        return Integer.parseInt(idParameter[1]);
    }


    private void getAllTasks(HttpExchange exchange) throws IOException {
        String responseString = gson.toJson(manager.getTasks());
        writeResponse(exchange, responseString, 200);
        System.out.println("Получили все задачи");
    }

    private void getAllEpics(HttpExchange exchange) throws IOException {
        String respnseString = gson.toJson(manager.getEpics());
        writeResponse(exchange, respnseString, 200);
        System.out.println("Получили все эпики");
    }

    private void getAllSubtasks(HttpExchange exchange) throws IOException {
        String responseString = gson.toJson(manager.getSubtasks());
        writeResponse(exchange, responseString, 200);
        System.out.println("Получили все подзадачи");
    }

    private void getPriorTasks(HttpExchange exchange) throws IOException {
        String responseString = gson.toJson(manager.getPrioritizedTasks());
        writeResponse(exchange, responseString, 200);
        System.out.println("Получили список задач по приоритету");
    }

    private void getHistory(HttpExchange exchange) throws IOException {
        String responseString = gson.toJson(manager.getHistory());
        writeResponse(exchange, responseString, 200);
        System.out.println("Получили историю");
    }

    private void createTask(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        Task task = gson.fromJson(body, Task.class);
        manager.createTask(task);
        writeResponse(exchange, "Задача создана успешно", 200);
        System.out.println("Создали задачу id = " + task.getId());
    }
    private void createEpic(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        Epic epic = gson.fromJson(body, Epic.class);
        manager.createEpic(epic);
        writeResponse(exchange, "Эпик создан успешно", 200);
        System.out.println("Создали задачу id = " + epic.getId());
    }

    private void createSubtask(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        Subtask subtask = gson.fromJson(body, Subtask.class);
        manager.createSubtask(subtask);
        writeResponse(exchange, "Подзадача создана успешно", 200);
        System.out.println("Создали подзадачу id = " + subtask.getId());
    }

    private void getTaskById(HttpExchange exchange) throws IOException {
        int id = getIdFromURI(exchange);
        String responseString = gson.toJson(manager.getTaskById(id));
        writeResponse(exchange, responseString, 200);
        System.out.println("Получили задачу с id = " + id);
    }

    private void getEpicById(HttpExchange exchange) throws IOException {
        int id = getIdFromURI(exchange);
        var epic = manager.getEpicById(id);
        String responseString = gson.toJson(epic, Epic.class);

        writeResponse(exchange, responseString, 200);
        System.out.println("Получили эпик с id = " + id);
    }

    private void getSubtaskById(HttpExchange exchange) throws IOException {
        int id = getIdFromURI(exchange);
        String responseString = gson.toJson(manager.getSubtaskById(id));
        writeResponse(exchange, responseString, 200);
        System.out.println("Получили подзадачу с id = " + id);
    }

    private void getAllSubtasksByEpicId(HttpExchange exchange) throws IOException {
        int id = getIdFromURI(exchange);
        String responseString = gson.toJson(manager.getAllSubtasksByEpicId(id));
        writeResponse(exchange, responseString, 200);
        System.out.println("Получили все подзадачи для эпика с id = " + id);
    }

    private void updateTask(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        Task task = gson.fromJson(body, Task.class);

        manager.updateTask(task);
        writeResponse(exchange, "Задача успешно обновленна", 200);
        System.out.println("Обновили задачу с id = " + task.getId());
    }

    private void updateEpic(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        Epic epic = gson.fromJson(body, Epic.class);

        manager.updateEpic(epic);
        writeResponse(exchange, "Задача успешно обновленна", 200);
        System.out.println("Обновили эпис с id = " + epic.getId());
    }

    private void updateSubtask(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        Subtask subtask = gson.fromJson(body, Subtask.class);

        manager.updateSubtask(subtask);
        writeResponse(exchange, "Задача успешно обновленна", 200);
        System.out.println("Обновили подзадачу с id = " + subtask.getId());
    }

    private void removeAllTasks(HttpExchange exchange) throws IOException {
        manager.removeAllTasks();
        writeResponse(exchange, "Все задачи удалены успешно", 200);
        System.out.println("Удалили все задачи");
    }

    private void removeAllEpics(HttpExchange exchange) throws IOException {
        manager.removeAllEpics();
        writeResponse(exchange, "Все эпики удалены успешно", 200);
        System.out.println("Удалили все эпики (подзадачи включительно)");
    }

    private void removeAllSubtasks(HttpExchange exchange) throws IOException {
        manager.removeAllSubtasks();
        writeResponse(exchange, "Все подзадачи удалены успешно", 200);
        System.out.println("Удалили все подзадачи");
    }

    private void deleteTaskById(HttpExchange exchange) throws IOException {
        int id = getIdFromURI(exchange);
        manager.deleteTaskByID(id);

        writeResponse(exchange, "Задача с id " + id + " удалена успешно", 200);
        System.out.println("Удалили задачу с id = " + id);
    }

    private void deleteEpicById(HttpExchange exchange) throws IOException {
        int id = getIdFromURI(exchange);
        manager.deleteEpicByID(id);

        writeResponse(exchange, "Эпик с id " + id + " удален успешно", 200);
        System.out.println("Удалили эпик (с подзадачами) с id = " + id);
    }

    private void deleteSubtaskById(HttpExchange exchange) throws IOException {
        int id = getIdFromURI(exchange);
        manager.deleteSubtaskByID(id);

        writeResponse(exchange, "Подзадача с id " + id + " удалена успешно", 200);
        System.out.println("Удалили подзадачу с id = " + id);
    }
}


