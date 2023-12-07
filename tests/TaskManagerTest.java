import controllers.Manager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends Manager>{
    protected Manager taskManager;    //Получение менеджера задач
    protected Task task1;
    Task task2;
    protected Epic epic1;
    Epic epic2;
    protected Subtask subtask1;
    protected Subtask subtask2;

    //Проверка функции получения списка всех Эпиков.
    @Test
    void getEpics(){    //ArrayList<Task> getEpics();
        //С пустым списком задач.
        assertDoesNotThrow(()->taskManager.getEpics(),
                "Запрос пустого списка задач не должен вызывать исключений!");

        //Со стандартным поведением.
        epic1 = new Epic("Переезд", "Телефон перевозчика: +123 456 78 90");
        taskManager.createEpic(epic1);
        assertEquals(1, taskManager.getEpics().size(), "Количество эпиков в истории не верно!");
    }

    //Проверка функции получения списка всех подзадач определённого эпика.
    @Test
    void getSubtasks(){
        //С пустым списком подзадач.
        epic1 = new Epic("Переезд", "Телефон перевозчика: +123 456 78 90");
        taskManager.createEpic(epic1);
        assertDoesNotThrow(()->taskManager.getSubtasks(),
                "Запрос пустого списка подзадач не должен вызывать исключений!");

        //a. Со стандартным поведением.
        subtask1 = new Subtask("Собрать коробки", "Коробки на чердаке", epic1.getId());
        taskManager.createSubtask(subtask1);
        assertEquals(1, taskManager.getSubtasks().size(), "Количество подзадач в эпике не верно!");
    }

    //Проверка функции получения списка всех задач.
    @Test
    void getTasks(){
        //С пустым списком задач.
        assertDoesNotThrow(()->taskManager.getTasks(),
                "Запрос пустого списка подзадач не должен вызывать исключений!");

        //Со стандартным поведением.
        task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.createTask(task1);
        assertEquals(1, taskManager.getTasks().size(), "Количество подзадач в эпике не верно!");
    }

    //Проверка функции получения задачи по идентификатору.
    @Test
    void getTaskById() {    //Task getTaskById(id);
        //С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        assertDoesNotThrow(()->taskManager.getTaskById(1),
                "Запрос несуществующей задачи не должен вызывать исключений!");

        //Со стандартным поведением.
        task1 = new Task("Задача 1", "Задача для удаления из начала истории");
        taskManager.createTask(task1);

        assertEquals(task1, taskManager.getTaskById(task1.getId()),
                "Возвращённая задача не соответствует добавленой!");
    }

    @Test
    void getSubtaskById() {    //Task getSubtaskById(id);
        //С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        assertDoesNotThrow(()->taskManager.getSubtaskById(1),
                "Запрос несуществующей задачи не должен вызывать исключений!");

        //Со стандартным поведением.
        epic1 = new Epic("Переезд", "Телефон перевозчика: +123 456 78 90");
        taskManager.createEpic(epic1);
        subtask1 = new Subtask("Собрать коробки", "Коробки на чердаке", epic1.getId());
        taskManager.createSubtask(subtask1);

        assertEquals(subtask1, taskManager.getSubtaskById(subtask1.getId()),
                "Возвращённая задача не соответствует добавленой!");
    }

    @Test
    void getEpicById() {    //Task getTaskById(id);
        //С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        assertDoesNotThrow(()->taskManager.getEpicById(1),
                "Запрос несуществующей задачи не должен вызывать исключений!");

        //Со стандартным поведением.
        epic1 = new Epic("Переезд", "Телефон перевозчика: +123 456 78 90");
        taskManager.createEpic(epic1);

        assertEquals(epic1, taskManager.getEpicById(epic1.getId()),
                "Возвращённая задача не соответствует добавленой!");
    }
    @Test
    void createTask() throws IOException, InterruptedException {    //createTask(Task task1);
        //a. Со стандартным поведением.
        task1 = new Task("Задача", "Задача для добавления");
        taskManager.createTask(task1);
        assertEquals(1, taskManager.getTasks().size(),
                "Количество задач в менеджере после добавления новой задачи не верно!");
    }

    @Test
    void createEpic() throws IOException, InterruptedException {    //createEpic(Epic epic1);
        //a. Со стандартным поведением.
        epic1 = new Epic("Эпик", "Эпик для добавления");
        taskManager.createEpic(epic1);
        assertEquals(1, taskManager.getEpics().size(),
                "Количество задач в менеджере после добавления новой задачи не верно!");
    }

    @Test
    void createSubtask() {    //createSubtask(Subtask subtask);
        //Со стандартным поведением.
        epic1 = new Epic("Эпик", "Эпик для добавления");
        taskManager.createEpic(epic1);
        subtask1 = new Subtask("Сабтаск", "Сабтаск для добавления", epic1.getId());
        taskManager.createSubtask(subtask1);
        assertEquals(1, taskManager.getSubtasks().size(),
                "Количество задач в менеджере после добавления новой задачи не верно!");
    }

    @Test
    void updateTask(){    //void updateTask(Task task1);
        //c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        taskManager.updateTask(null);
        assertDoesNotThrow(()->taskManager.updateTask(null),
                "Попытка пустого обновления не должна вызывать исключение!");

        //a. Со стандартным поведением.
        task1 = new Task("Задача 1", "Задача для проверки обновления");
        taskManager.createTask(task1);

        task1 = new Task("Задача 1", "Обновлённый вариант задачи");
        taskManager.updateTask(task1);
        assertEquals("Обновлённый вариант задачи", task1.getDescription(),
                "Описание задачи не было обновлено!");
    }

    @Test
    void updateEpic() {    //void updateEpic(Epic epic1);
        //c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        taskManager.updateEpic(null);
        assertDoesNotThrow(()->taskManager.updateEpic(null),
                "Попытка пустого обновления не должна вызывать исключение!");

        //a. Со стандартным поведением.
        epic1 = new Epic("Эпик 1", "Эпик для проверки обновления");
        taskManager.createEpic(epic1);

        epic1 = new Epic("Эпик 1", "Обновлённый вариант Эпика");
        taskManager.updateEpic(epic1);
        assertEquals("Обновлённый вариант Эпика", epic1.getDescription(),
                "Описание Эпика не было обновлено!");
    }

    @Test
    void updateSubtask(){    //void updateTask(Task task1);
        //c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).

        taskManager.updateSubtask(null);
        assertDoesNotThrow(()->taskManager.updateSubtask(null),
                "Попытка пустого обновления не должна вызывать исключение!");

        //a. Со стандартным поведением.
        epic1 = new Epic("Эпик 1", "Эпик для проверки обновления");
        taskManager.createEpic(epic1);

        subtask1 = new Subtask("Подзадача 1", "Подзадача для проверки обновления", epic1.getId());
        taskManager.createSubtask(subtask1);

        subtask1 = new Subtask("Подзадача 1", "Обновлённый вариант подзадачи", subtask1.getId(), epic1.getId());
        taskManager.updateSubtask(subtask1);
        assertEquals("Обновлённый вариант подзадачи", subtask1.getDescription(),
                "Описание подзадачи не было обновлено!");
    }

    //Проверка функции удаления ранее добавленных задач — всех и по идентификатору.
    @Test
    void deleteTasks(){
        //b. С пустым списком задач.
        assertDoesNotThrow(()->taskManager.deleteTaskByID(0),
                "Попытка удаления несуществующей задачи не должна вызывать исключение!");

        //a. Со стандартным поведением.
        task1 = new Task("Задача 1", "Задача для проверки удаления");
        taskManager.createTask(task1);
        task2 = new Task("Задача 2", "Задача для проверки удаления");
        taskManager.createTask(task2);
        assertEquals(2, taskManager.getTasks().size(),
                "Количество задач в менеджере после добавления новой задачи не верно!");
        taskManager.deleteTaskByID(task1.getId());
        assertEquals(1, taskManager.getTasks().size(),
                "Количество задач в менеджере после удаления задачи не верно!");

        //c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        taskManager.removeAllTasks();  //Пустой идентификатор означает удаление всех задач
        assertEquals(0, taskManager.getTasks().size(),
                "После удаления всех задач список не пустой!");


    }

    @Test
    void deleteEpics(){
        //b. С пустым списком задач.
        assertDoesNotThrow(()->taskManager.deleteEpicByID(3),
                "Попытка удаления несуществующей задачи не должна вызывать исключение!");

        //a. Со стандартным поведением.
        epic1 = new Epic("Эпик 1", "Эпик для проверки удаления");
        taskManager.createEpic(epic1);
        epic2 = new Epic("Эпик 2", "Эпик для проверки удаления");
        taskManager.createEpic(epic2);
        assertEquals(2, taskManager.getEpics().size(),
                "Количество эпиков в менеджере после добавления нового эпика не верно!");
        taskManager.deleteEpicByID(epic1.getId());
        assertEquals(1, taskManager.getEpics().size(),
                "Количество эпиков в менеджере после удаления эпика не верно!");

        //c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        taskManager.removeAllEpics();  //Пустой идентификатор означает удаление всех задач
        assertEquals(0, taskManager.getEpics().size(),
                "После удаления всех эпиков список не пустой!");
    }

    @Test
    void deleteSubtasks(){
        //b. С пустым списком задач.
        assertDoesNotThrow(()->taskManager.deleteSubtaskByID(1),
                "Попытка удаления несуществующей задачи не должна вызывать исключение!");

        //a. Со стандартным поведением.
        epic1 = new Epic("Эпик 1", "Эпик для проверки удаления");
        taskManager.createEpic(epic1);
        subtask1 = new Subtask("Сабтаск 1", "Сабтаск для проверки удаления", epic1.getId());
        taskManager.createSubtask(subtask1);
        subtask2 = new Subtask("Сабтаск 2", "Сабтаск для проверки удаления", epic1.getId());
        taskManager.createSubtask(subtask2);
        assertEquals(2, taskManager.getSubtasks().size(),
                "Количество сабтасков в менеджере после добавления нового сабтаска не верно!");
        taskManager.deleteSubtaskByID(subtask1.getId());
        assertEquals(1, taskManager.getSubtasks().size(),
                "Количество сабтасков в менеджере после удаления сабтасков не верно!");

        //c. С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
        taskManager.removeAllSubtasks();  //Пустой идентификатор означает удаление всех задач
        assertEquals(0, taskManager.getSubtasks().size(),
                "После удаления всех сабтасков список не пустой!");
    }

    //Проверка функции получения просмотренных задачи.
    // (полученных через getTask(), изменённых updateTask() или удалённых delTask()).
    @Test
    void history(){    //ArrayList<Task> history();
        //b. С пустым списком задач.
        assertDoesNotThrow(()->taskManager.getHistory(),
                "Запрос пустой истории обращений не должен вызывать исключений!");

        assertNotNull(taskManager.getHistory(), "История не пустая!");

        //a. Со стандартным поведением.
        task1 = new Task("Задача 1", "Задача для удаления из начала истории");
        taskManager.createTask(task1);
        task1 = taskManager.getTaskById(task1.getId());
        assertEquals(1, taskManager.getHistory().size(),
                "Размер истории обращений не верен!");
    }

    @Test
    void getPrioritizedTasks(){
        assertNotNull(taskManager.getPrioritizedTasks(), "Список задач не пустой!");

        epic1 = new Epic("Эпик 1", "Пустой эпик");
        taskManager.createEpic(epic1);

        epic2 = new Epic("Эпик 2", "Эпик с подзадачами");
        taskManager.createEpic(epic2);

        subtask1 = new Subtask("Собрать коробки",
                "Коробки на чердаке",
                LocalDateTime.now().plusMinutes(100),
                Duration.ofMinutes(30),
                epic1.getId());
        taskManager.createSubtask(subtask1);

        subtask2 = new Subtask("Упаковать кошку",
                "Переноска за дверью",
                LocalDateTime.now().plusMinutes(300),
                Duration.ofHours(1).plusMinutes(30),
                epic2.getId());
        taskManager.createSubtask(subtask2);

        task1 = new Task("Задача 1",
                "Задача для наполнения менеджера",
                LocalDateTime.now().plusMinutes(200),
                Duration.ofHours(1).plusMinutes(15));
        taskManager.createTask(task1);

        assertEquals(3, taskManager.getPrioritizedTasks().size(),
                "Количество задач в отсортированном списке не верно!");
    }
}