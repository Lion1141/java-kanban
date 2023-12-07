package controllers;

import exceptions.ManagerSaveException;
import model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static model.TaskType.EPIC;
import static model.TaskType.SUBTASK;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final static String PATH_TO_SAVE = "resources/";
    private final String fileName;

    public FileBackedTasksManager(String fileName) {
        this.fileName = fileName;
    }
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_TO_SAVE + fileName))) {
            for (Task task : tasks.values()) {
                    writeTaskToFile(writer, task);
            }

            for (Epic epic : epics.values()) {
                    writeTaskToFile(writer, epic);
            }

            for (Subtask subtask : subtasks.values()) {
                    writeTaskToFile(writer, subtask);
            }

            String historyInString = historyToString(getHistoryManager());

            writer.newLine();
            writer.write(historyInString);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла");
        }
    }


    private void writeTaskToFile(BufferedWriter writer, Task task) throws IOException {
        writer.write(taskToString(task));
        writer.newLine();
    }

    private String taskToString(Task task) {
        String row = String.format("%s,%s,%s,%s,%s,%s,%s,%s", task.getId(),
                task.getTaskType(),
                task.getName(),
                task.getDescription(),
                task.getStatus(),
                task.getStartTime(),
                task.getDuration(),
                task.getEndTime());

        if (task.getTaskType() == SUBTASK) {
            long epicId = ((Subtask) task).getIdEpic();

            row = row.concat(",").concat(Long.toString(epicId));
        }

        return row;
    }

    private static void addTask(FileBackedTasksManager tasksManager, Task task) {
        final int id = task.getId();
        switch (task.getTaskType()) {
            case EPIC:
                tasksManager.epics.put(id, (Epic) task);
                break;
            case SUBTASK:
                tasksManager.subtasks.put(id, (Subtask) task);
                break;
            default:
                tasksManager.tasks.put(id, task);
        }
    }

    protected static void addHistory(FileBackedTasksManager tasksManager, List<Integer> history) {
        for (Integer id : history) {
            Task task = findTask(tasksManager, id);
            tasksManager.getHistoryManager().addTask(task);
        }
    }

    private static void addSubtaskToEpic(FileBackedTasksManager tasksManager) {
        for (Map.Entry<Integer, Subtask> entry : tasksManager.subtasks.entrySet()) {
            final Subtask subtask = entry.getValue();
            final Epic epic = tasksManager.epics.get(subtask.getIdEpic());

            var idSubtasks = epic.getSubtaskslist();
            if(idSubtasks.contains(subtask.getId())) {
                return;
            }
            idSubtasks.add(subtask);
        }
    }

    private static Task findTask(FileBackedTasksManager tasksManager, int id) {
        final Epic epic = tasksManager.epics.get(id);
        if (epic != null)
            return epic;

        final Subtask subtask = tasksManager.subtasks.get(id);
        if (subtask != null)
            return subtask;

        return tasksManager.tasks.get(id);
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        int counterId = 0;
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file.getName());

        try {
            List<String> rows = Files.readAllLines(Path.of(file.toURI()));

            for (String row : rows) {
                if (row.isEmpty())
                    break;

                Task task = taskFromString(row);
                addTask(tasksManager, task);

                int taskId = task.getId();
                counterId = Math.max(counterId, taskId);
            }


            addSubtaskToEpic(tasksManager);

            String historyString = rows.get(rows.size() - 1);
            List<Integer> history = historyFromString(historyString);
            addHistory(tasksManager, history);
        } catch (IOException e) {
            System.out.printf("Невозможно найти файл для загрузки: %s\n", file);
        }

        return tasksManager;
    }

    private static Task taskFromString(String value) {
        final String[] taskValues = value.split(",");

        final int taskId = Integer.parseInt(taskValues[0]);
        final TaskType taskType = TaskType.valueOf(taskValues[1]);
        final String taskName = taskValues[2];
        final String taskDescription = taskValues[3];
        final TaskStatus taskStatus = TaskStatus.valueOf(taskValues[4]);

        if (taskType == EPIC) {
            return new Epic(taskName, taskDescription, taskId);
        } else {
            final LocalDateTime taskStartTime = LocalDateTime.parse(taskValues[5]);
            final Duration taskDuration = Duration.parse(taskValues[6]);
            if (taskType == SUBTASK) {
                final int epicId = Integer.parseInt(taskValues[5]);
                return new Subtask(taskName, taskDescription, taskStatus, taskId, taskStartTime, taskDuration, epicId);
            } else {
                return new Task(taskName, taskDescription, taskId, taskStatus, taskStartTime, taskDuration);
            }
        }
    }



    protected static String historyToString(HistoryManager manager) {
        StringBuilder builder = new StringBuilder();
        for (Task taskHistory : manager.getHistory()) {
            builder.append(taskHistory.getId()).append(",");
        }

        return builder.toString();
    }

    protected static List<Integer> historyFromString(String value) {
        List<Integer> history = new LinkedList<>();
        String[] elements = value.split(",");

        for (String element : elements) {
            if(!element.isBlank()){
                history.add(Integer.parseInt(element));
            }
        }

        return history;
    }


    @Override
    public Integer createTask(Task task) {
        super.createTask(task);
        save();
        return task.getId();
    }

    @Override
    public Integer createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public void addSubtaskToEpics(Subtask subtask, int idEpic) {
        super.addSubtaskToEpics(subtask, idEpic);
        save();
    }

    @Override
    public void removeIdSubtaskFromEpics(int idSubtask, int idEpic) {
        super.removeIdSubtaskFromEpics(idSubtask, idEpic);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int idTask) {
        Task task = super.getTaskById(idTask);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int idEpic) {
        Epic epic = super.getEpicById(idEpic);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int idSubtask) {
        Subtask subtask = super.getSubtaskById(idSubtask);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.  updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskByID(Integer id) {
        super.deleteTaskByID(id);
        save();
    }

    @Override
    public void deleteEpicByID(Integer id) {
        super.deleteEpicByID(id);
        save();
    }

    @Override
    public void deleteSubtaskByID(Integer id) {
        super.deleteSubtaskByID(id);
        save();
    }

    @Override
    public void checkStatusOfSubtask(Integer idEpic) {
        super.checkStatusOfSubtask(idEpic);
        save();
    }

    public static void main(String[] args) {
        FileBackedTasksManager manager = loadFromFile(new File("resources/fileToSave.csv"));

        Task task1 = new Task("Задача 1", "Описание задачи 1"); //создаём задачи
        Integer taskId1 = manager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        Integer taskId2 = manager.createTask(task2);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Integer epicId1 = manager.createEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        Integer epicId2 = manager.createEpic(epic2);
        Subtask subtask21 = new Subtask("Сабтаск 2.1","Описание сабтаска 2.1", epic2.getId());
        Integer subtaskId21 = manager.createSubtask(subtask21);
        Subtask subtask22 = new Subtask("Сабтаск 2.2", "Описание сабтаска 2.2", epic2.getId());
        Integer subtaskId22 = manager.createSubtask(subtask22);
        Subtask subtask23 = new Subtask("Сабтаск 2.3", "Описание сабтаска 2.3", epic2.getId());
        Integer subtaskId23 = manager.createSubtask(subtask23);



        System.out.println("**************Список задач**************"); //выводим на консоль
        System.out.println(task1);
        System.out.println(task2);
        System.out.println();
        System.out.println("**************Список эпиков с подзадачами**************");
        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println(subtask21);
        System.out.println(subtask22);
        System.out.println(subtask23);
        System.out.println();

        manager.getTaskById(0);
        manager.getTaskById(0);

        manager.getEpicById(3);
        manager.getEpicById(3);
        manager.getEpicById(3);

        manager.getSubtaskById(6);
        manager.getSubtaskById(4);
        manager.getSubtaskById(5);
        manager.getSubtaskById(5);

        manager.getTaskById(1);
        manager.getTaskById(1);

        manager.deleteSubtaskByID(subtask22.getId()); //удаляем сабтаск
        //System.out.println(manager.getSubtasks()); //проверяем удаление

        System.out.println("**************История задач**************");
        List<Task> history = manager.getHistory();
    }
}
