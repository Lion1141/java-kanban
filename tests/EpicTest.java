import controllers.Manager;
import model.Epic;
import model.Subtask;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;

import static model.TaskStatus.DONE;
import static model.TaskStatus.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    Manager manager;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    //Тестирование процедуры получения статуса Эпика
    @Test
    public void shouldHasNewStatusWithEmptySubtasks() {
        epic = (new Epic("Epic1", "Epic1 desc"));
        manager.createEpic(epic);
        TaskStatus status = manager.getEpicById(epic.getId()).getStatus();

        assertEquals(TaskStatus.NEW, status, "Different statuses");
    }

    @Test
    public void shouldHasNewStatusWithAllNewStatusForSubtasks() {
        epic = new Epic("Epic1", "Epic1 desc");
        manager.createEpic(epic);
        subtask1 = new Subtask("Subtask1 Epic1", "Subtask1 Epic1 desc", epic.getId());
manager.createSubtask(subtask1);
        manager.createSubtask(new Subtask("Subtask2 Epic1", "Subtask1 Epic1 desc", epic.getId()));

        TaskStatus status = manager.getEpicById(epic.getId()).getStatus();

        assertEquals(TaskStatus.NEW, status, "Different statuses");
    }

    @Test
    public void shouldHasDoneStatusWithAllDoneStatusForSubtasks() {
        epic = new Epic("Epic1", "Epic1 desc");
        manager.createEpic(epic);
        subtask1 = new Subtask("Subtask1 Epic1", "Subtask1 Epic1 desc", epic.getId());
        manager.createSubtask(subtask1);
        subtask2 = new Subtask("Subtask2 Epic1", "Subtask2 Epic1 desc", epic.getId());
        manager.createSubtask(subtask2);
        manager.updateSubtask(new Subtask("Subtask2 Epic1",
                "Subtask1 Epic1 desc",
                subtask1.getId(),
                DONE,
                epic.getId()));
        manager.updateSubtask(new Subtask("Subtask2 Epic1",
                "Subtask1 Epic1 desc",
                subtask2.getId(),
                DONE,
                epic.getId()));

        TaskStatus status = manager.getEpicById(epic.getId()).getStatus();

        assertEquals(DONE, status, "Different statuses");
    }

    @Test
    public void shouldHasInProgressStatusWithNewAndDoneStatusesForSubtasks() {
        epic = new Epic("Epic1", "Epic1 desc");
        manager.createEpic(epic);
        subtask1 = new Subtask("Subtask1 Epic1", "Subtask1 Epic1 desc", epic.getId());
        manager.createSubtask(subtask1);
        subtask2 = new Subtask("Subtask2 Epic1", "Subtask2 Epic1 desc", epic.getId());
        manager.createSubtask(subtask2);
        manager.updateSubtask(new Subtask("Subtask2 Epic1",
                "Subtask1 Epic1 desc",
                subtask1.getId(),
                DONE,
                epic.getId()));

        TaskStatus status = manager.getEpicById(epic.getId()).getStatus();

        assertEquals(IN_PROGRESS, status, "Different statuses");
    }

    @Test
    public void shouldHasInProgressStatusWithAllInProgressStatusesForSubtasks() {
        epic = new Epic("Epic1", "Epic1 desc");
        manager.createEpic(epic);
        subtask1 = new Subtask("Subtask1 Epic1", "Subtask1 Epic1 desc", epic.getId());
        manager.createSubtask(subtask1);
        subtask2 = new Subtask("Subtask2 Epic1", "Subtask2 Epic1 desc", epic.getId());
        manager.createSubtask(subtask2);
        manager.updateSubtask(new Subtask("Subtask2 Epic1",
                "Subtask1 Epic1 desc",
                subtask1.getId(),
                IN_PROGRESS,
                epic.getId()));
        manager.updateSubtask(new Subtask("Subtask2 Epic1",
                "Subtask1 Epic1 desc",
                subtask2.getId(),
                IN_PROGRESS,
                epic.getId()));

        TaskStatus status = manager.getEpicById(epic.getId()).getStatus();

        assertEquals(IN_PROGRESS, status, "Different statuses");
    }
}
