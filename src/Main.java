import controllers.Manager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
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
        Subtask subtask11 = new Subtask("Сабтаск 1.1","Описание сабтаска 1.1", epic1.getId());
        Integer subtaskId11 = manager.createSubtask(subtask11);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        Integer epicId2 = manager.createEpic(epic2);
        Subtask subtask21 = new Subtask("Сабтаск 2.1","Описание сабтаска 2.1", epic2.getId());
        Integer subtaskId21 = manager.createSubtask(subtask21);
        Subtask subtask22 = new Subtask("Сабтаск 2.2", "Описание сабтаска 2.2", epic2.getId());
        Integer subtaskId22 = manager.createSubtask(subtask22);


        System.out.println("**************Список задач**************"); //выводим на консоль
        System.out.println(task1);
        System.out.println(task2);
        System.out.println();
        System.out.println("**************Список эпиков с подзадачами**************");
        System.out.println(epic1);
        System.out.println(subtask11);
        System.out.println(epic2);
        System.out.println(subtask21);
        System.out.println(subtask22);
        System.out.println();

        System.out.println("**************Обновляем задачи**************");
        task1.setName("Задача ver.2");
        task2.setDescription("Описание задачи ver.2");
        task1.setStatus(TaskStatus.IN_PROGRESS); //выбираем статус, который хотим присвоить в обновлении
        manager.updateTask(task1); //обновляем задачу
        System.out.println(task1); //выводим обновлённую задачу

        task2.setStatus(TaskStatus.DONE);
        manager.updateTask(task2);
        System.out.println(task2);
        System.out.println();

        System.out.println("**************Обновляем эпики**************");
        epic1.setName("Новый эпик 1");
        epic1.setDescription("Новое описание эпика 1");
        manager.updateEpic(epic1);
        subtask11.setName("Новый сабтаск 1.1");
        subtask11.setDescription("Новое описание сабтаска 1.1");
        subtask11.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask11); //меняем статус сабтаска
        System.out.println(epic1); //вывод эпика с новым статусом

        manager.updateSubtask(subtask21); //меняем статус сабтаска
        System.out.println(epic2); //вывод эпика со старым статусом
        System.out.println();

        System.out.println("**************Удалим задачу, эпик 1 и сабтаск эпика 2**************");

//        manager.deleteEpicByID(epic1.getId());
//        manager.deleteTaskByID(task2.getId());
//        manager.deleteSubtaskByID(subtask22.getId());
//        manager.removeIdSubtaskFromEpics(subtask21.getId(), epic2.getId());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
//        manager.removeAllSubtasks();
        System.out.println(manager.getEpics());

        System.out.println(manager.getIdTask(1));
        System.out.println(manager.getIdEpic(4));
        System.out.println(manager.getSubtaskById(5));


        System.out.println("История задач");
        List<Task> history = manager.getHistory();
        System.out.println(history);

    }
}