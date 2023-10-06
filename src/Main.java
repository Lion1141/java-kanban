import controllers.Manager;
import model.Epic;
import model.Subtask;
import model.Task;
import util.Managers;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Manager manager = Managers.getDefault();

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
        System.out.println(history);


    }
}