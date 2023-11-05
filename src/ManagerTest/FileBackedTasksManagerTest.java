package ManagerTest;

import controllers.FileBackedTasksManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    FileBackedTasksManager taskManager1;

    @BeforeEach
    public void beforeEach() {
        taskManager =  new FileBackedTasksManager("fileToSave.csv");
    }

    //Тестирование метода сохранения задач в файл
    @Test
    void save() {
        //a. Пустой список задач.
        assertDoesNotThrow(()->((FileBackedTasksManager)taskManager).save(),
                "Сохранение менеджера с пустогым списком задач не должно вызывать исключений!");

        //b. Эпик без подзадач.
        epic1 = new Epic("Переезд", "Телефон перевозчика: +123 456 78 90");
        taskManager.createEpic(epic1);
        ((FileBackedTasksManager)taskManager).save();
        taskManager1 = ((FileBackedTasksManager)taskManager).loadFromFile(new File("resources/fileToSave.csv"));
        assertEquals(1, taskManager1.getEpics().size(),
                "Количество задач менеджера после восстановления не совпало!");

        //c. Пустой список истории.
        assertEquals(0, taskManager1.getHistory().size(),
                "Количество задач в истории обращения после восстановления не совпало!");

    }

    //Тестирование метода загрузки списка задач из файла
    @Test
    void loadFromFile() {
        //c. Пустой список истории.
        assertEquals(0, taskManager.getHistory().size(),
                "Количество задач в истории обращения после восстановления не совпало!");

        //a. Со стандартным поведением.
        epic1 = new Epic("Эпик 2", "Эпик с подзадачами");
        taskManager.createEpic(epic1);
        subtask1 = new Subtask("Собрать коробки",
                "Коробки на чердаке",
                LocalDateTime.now(),
                Duration.ofHours(1).plusMinutes(30),
                epic1.getId());
        taskManager.createSubtask(subtask1);

        subtask2 = new Subtask(
                "Упаковать кошку",
                "Переноска за дверью",
                LocalDateTime.now().plusHours(2),
                Duration.ofHours(1).plusMinutes(30),
                epic1.getId());
        taskManager.createSubtask(subtask2);

        task1 = new Task("Задача 1",
                "Задача для наполнения менеджера",
                LocalDateTime.now().plusHours(4),
                Duration.ofHours(2).plusMinutes(15));
        taskManager.createTask(task1);

        taskManager.getTaskById(task1.getId());   //Добавление истории

        taskManager1 = ((FileBackedTasksManager)taskManager).loadFromFile(new File("resources/fileToSave.csv"));
        assertEquals(3, taskManager1.getTasks().size(),
                "Количество задач менеджера после восстановления не совпало!");

    }

}