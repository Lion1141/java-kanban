import controllers.Manager;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
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
        System.out.println(task1.toString());
        System.out.println(task2.toString());
        System.out.println();
        System.out.println("**************Список эпиков с подзадачами**************");
        System.out.println(epic1.toString());
        System.out.println(subtask11.toString());
        System.out.println(epic2.toString());
        System.out.println(subtask21.toString());
        System.out.println(subtask22.toString());
        System.out.println();

        System.out.println("**************Обновляем задачи**************");
        task1.setName("Задача ver.2");
        task2.setDescription("Описание задачи ver.2");
        task1.setStatus("IN_PROGRESS"); //выбираем статус, который хотим присвоить в обновлении
        manager.updateTask(task1); //обновляем задачу
        System.out.println(task1.toString()); //выводим обновлённую задачу

        task2.setStatus("DONE");
        manager.updateTask(task2);
        System.out.println(task2.toString());
        System.out.println();

        System.out.println("**************Обновляем эпики**************");
        epic1.setName("Новый эпик 1");
        epic1.setDescription("Новое описание эпика 1");
        manager.updateEpic(epic1);
        subtask11.setName("Новый сабтаск 1.1");
        subtask11.setDescription("Новое описание сабтаска 1.1");
        subtask11.setStatus("DONE");
        manager.updateSubtask(subtask11); //меняем статус сабтаска
        System.out.println(epic1.toString()); //вывод эпика с новым статусом

        manager.updateSubtask(subtask21);; //меняем статус сабтаска
        System.out.println(epic2.toString()); //вывод эпика со старым статусом
        System.out.println();

        System.out.println("**************Удалим задачу, сабтаски эпика 1 и эпик 2**************");

        manager.deleteById(epic1.getId());
        manager.deleteById(task2.getId());
        manager.removeIdSubtaskFromEpics(subtask21.getId(), epic2.getId());
        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
    }
}