import controllers.Manager;
import model.Epic;
import model.Subtask;
import model.Task;

import static model.Task.STATUS_DONE;
import static model.Task.STATUS_IN_PROGRESS;


public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = manager.createTask("Задача 1", "Описание задачи 1"); //создаём задачи
        Task task2 = manager.createTask("Задача 2", "Описание задачи 2");
        Epic epic1 = manager.createEpic("Эпик 1", "Описание эпика 2");
        Subtask subtask = manager.createSubtask(epic1.getId(), "Сабтаск 1.1", "Описание сабтаска 1.1");
        Epic epic2 = manager.createEpic("Эпик 2", "Описание эпика 2");
        Subtask subtask1 = manager.createSubtask(epic2.getId(), "Сабтаск 2.1", "Описание сабтаска 2.1");
        Subtask subtask2 = manager.createSubtask(epic2.getId(), "Сабтаск 3.1", "Описание сабтаска 3.1");

        System.out.println("**************Список задач**************"); //выводим на консоль
        System.out.println(task1.toString());
        System.out.println(task2.toString());
        System.out.println();
        System.out.println("**************Список эпиков с подзадачами**************");
        System.out.println(epic1.toString());
        System.out.println(subtask.toString());
        System.out.println(epic2.toString());
        System.out.println(subtask1.toString());
        System.out.println(subtask2.toString());
        System.out.println();

        System.out.println("**************Обновляем задачи**************");
        task1.setName("Задача ver.2");
        task2.setDescription("Описание задачи ver.2");
        task1.setStatus(STATUS_IN_PROGRESS); //выбираем статус, который хотим присвоить в обновлении
        manager.updateTask(task1); //обновляем задачу
        System.out.println(task1.toString()); //выводим обновлённую задачу

        task2.setStatus(STATUS_DONE);
        manager.updateTask(task2);
        System.out.println(task2.toString());
        System.out.println();
        System.out.println("**************Обновляем эпики**************");
        epic1.setName("Новый эпик 1");
        epic1.setDescription("Новое описание эпика 1");
        manager.updateEpic(epic1);
        manager.changeSubtaskStatus(epic1.getId(), subtask.getId(), STATUS_IN_PROGRESS); //меняем статус сабтаска
        System.out.println(epic1.toString()); //вывод эпика с новым статусом

        manager.changeSubtaskStatus(epic2.getId(), subtask1.getId(), STATUS_DONE); //меняем статус сабтаска
        manager.changeSubtaskStatus(epic2.getId(), subtask2.getId(), STATUS_IN_PROGRESS); //меняем статус сабтаска
        System.out.println(epic2.toString()); //вывод эпика со старым статусом
        System.out.println();

        System.out.println("**************Удалим задачу, сабтаски эпика 1 и эпик 2**************");
        manager.removeIdEpic(epic1.getId());
        manager.removeIdTask(task1.getId());

        manager.removeAllEpicSubtasks(epic2.getId());

        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
    }
}