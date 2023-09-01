public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();
        //создаем объекты
        manager.createTask(new Task("Задача 1", "описание Задачи 1"));
        manager.createTask(new Task("Задача 2", "описание Задачи 2"));

        manager.createTask(new Epic("Эпик 1", "описание Эпик 1"));
        manager.createTask(new Subtask("Подзадача 1", "описание Подзадачи 1", 3));
        manager.createTask(new Subtask("Подзадача 2", "описание Подзадачи 2", 3));

        manager.createTask(new Epic("Эпик 2", "описание Эпик 1"));
        manager.createTask(new Subtask("Сабтаск 1", "описание Сабтаск 1", 6));

        //печатаем все объекты
        manager.printAllTask();
        //2.5 Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        //изменяем имя, описание, статус у Task с id 2
        manager.updateTask(new Task("Обновленное имя", "Обновленное описание", 2, "DONE"));
        //изменяем имя, описание у Epic с id 3
        manager.updateEpic(new Epic("Обновленное имя", "Обновленное описание", 3));
        //изменяем имя, описание, статус, Epic у Subtask с id 4
        manager.updateSubtask(new Subtask("Обновленное имя", "Обновленное описание", 4, "DONE", 3));
        //изменяем имя, описание, статус у Subtask с id 7
        manager.updateSubtask(new Subtask("Обновленное имя", "Обновленное описание", 7, "NEW", 6));
        //изменяем имя, описание, статус у Subtask с id 7
        manager.updateSubtask(new Subtask("Обновленное имя", "Обновленное описание", 5, "DONE", 6));
        //2.1 получение списка всех задач
        manager.printAllTask();
        //2.3 получение по идентификатору
        manager.getById(4);
        //3.1 получение списка всех подзадач определённого эпика
        manager.printEpicSubtask(3);
        //2.6 удаление по идентификатору
        manager.deleteById(3);
        //2.1 получение списка всех задач
        manager.printAllTask();
        //2.2 удаление всех задач
        manager.clearAllTasks();
    }
}
