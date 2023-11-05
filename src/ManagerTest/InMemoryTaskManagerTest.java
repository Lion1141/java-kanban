package ManagerTest;

import controllers.InMemoryTaskManager;
import controllers.Manager;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest{

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();    //Получение менеджера задач
    }
}