package ManagerTest;

import controllers.InMemoryHistoryManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager;
    Task task;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    //Тестирование процедуры добавления задачи в историю
    @Test
    void add() {
        //a. Пустая история задач.
        assertNotNull(historyManager, "История не пустая!");
        assertEquals(0, historyManager.getHistory().size(), "В пустой истории не должно быть элементов!");

        task = new Task("Задача 1", "Задача для проверки дублирования.", 0);

        historyManager.addTask(task);
        assertEquals(1, historyManager.getHistory().size(), "В истории должна быть одна задача!");

        //b. Дублирование.
        historyManager.addTask(task);  //Повторное добавление той же задачи
        assertEquals(1, historyManager.getHistory().size(), "В истории должна быть одна задача!");
    }

    //Тестирование процедуры удаления задачи из истории
    @Test
    void remove() {
        //a. Пустая история задач.
        assertDoesNotThrow(()-> historyManager.remove(1), "Удаление из пустой истории не должно вызывать исключений!");

        //с. Удаление из истории: начало, середина, конец.
        task = new Task("Задача 1", "Задача для удаления из начала истории", 0);
        historyManager.addTask(task);

        task = new Task("Задача 2", "Промежуточная задача для тестирования удаления", 1);
        historyManager.addTask(task);

        task = new Task("Задача 3", "Задача для удаления из середины истории", 2);
        historyManager.addTask(task);

        task = new Task("Задача 3", "Задача для удаления с конца истории",3);
        historyManager.addTask(task);

        historyManager.remove(0);
        assertEquals(3, historyManager.getHistory().size(), "Задача в начале истории не была удалена!");

        historyManager.remove(1);
        assertEquals(2, historyManager.getHistory().size(), "Задача из середины истории не была удалена!");

        historyManager.remove(2);
        assertEquals(1, historyManager.getHistory().size(), "Задача в конце истории не была удалена!");
    }

    //Тестирование процедуры получения истории
    @Test
    void getHistory() {
        //a. Пустая история задач.
        assertNotNull(historyManager, "История не пустая!");
        assertEquals(0, historyManager.getHistory().size(), "История должна быть пустой!");

        task = new Task("Задача 1", "Задача для проверки дублирования.");

        historyManager.addTask(task);
        assertEquals(1, historyManager.getHistory().size(), "В истории должна быть одна задача!");

        //b. Дублирование.
        historyManager.addTask(task);  //Повторное добавление той же задачи
        assertEquals(1, historyManager.getHistory().size(), "В истории должна быть одна задача!");

        //с. Удаление из истории: начало, середина, конец.
        task = new Task("Задача 1", "Задача для удаления из начала истории", 0);
        historyManager.addTask(task);

        task = new Task("Задача 2", "Промежуточная задача для тестирования", 1);
        historyManager.addTask(task);

        task = new Task("Задача 3", "Задача для удаления из середины истории", 2);
        historyManager.addTask(task);

        task = new Task("Задача 3", "Задача для удаления с конца истории", 3);
        historyManager.addTask(task);

        historyManager.remove(0);
        assertEquals(3, historyManager.getHistory().size(), "Задача в начале истории не была удалена!");

        historyManager.remove(1);
        assertEquals(2, historyManager.getHistory().size(), "Задача из середины истории не была удалена!");

        historyManager.remove(2);
        assertEquals(1, historyManager.getHistory().size(), "Задача в конце истории не была удалена!");
    }
}
