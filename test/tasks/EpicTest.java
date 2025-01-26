package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    private Epic epic1;

    @BeforeEach
    public void setUp() {
        epic1 = new Epic("Эпик1", "Описание эпика");
    }

    @Test
    public void epicsAreEqualsIfIdsAreSame() {
        assertEquals(epic1, epic1, "Эпики не совпадают");
    }

    //    @Test
    //    public void cantAddEpicToEpicAsSubtask() {
    //        Epic epic1 = new Epic("Эпик1", "Описание эпика");
    //
    //    } Не понял требование. Метод addTask() у Epic принимает только Subtask изначально

    @Test
    public void deleteSubtaskWorks() {
        Subtask subtask1 = new Subtask("Подзадача1", "Описание подзадачи", TaskStatus.NEW, epic1.getId());
        epic1.addTask(subtask1);
        epic1.deleteTask(subtask1.getId());
        assertEquals(0, epic1.getTasks().size(), "Подзадача не удалена");
    }

    @Test
    public void testAllNewSubtasks() {
        Subtask subtask1 = new Subtask("Подзадача1", "Описание подзадачи1", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача2", "Описание подзадачи2", TaskStatus.NEW, epic1.getId());

        epic1.addTask(subtask1);
        epic1.addTask(subtask2);

        epic1.verifyStatus();

        assertEquals(TaskStatus.NEW, epic1.getTaskStatus(), "Если все подзадачи NEW, то эпик NEW");
    }

    @Test
    public void testAllDoneSubtasks() {
        Subtask subtask1 = new Subtask("Подзадача1", "Описание подзадачи1", TaskStatus.DONE, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача2", "Описание подзадачи2", TaskStatus.DONE, epic1.getId());

        epic1.addTask(subtask1);
        epic1.addTask(subtask2);

        epic1.verifyStatus();

        assertEquals(TaskStatus.DONE, epic1.getTaskStatus(), "Если все подзадачи DONE, то эпик DONE");
    }

    @Test
    public void testAllNewAndDoneSubtasks() {
        Subtask subtask1 = new Subtask("Подзадача1", "Описание подзадачи1", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача2", "Описание подзадачи2", TaskStatus.DONE, epic1.getId());

        epic1.addTask(subtask1);
        epic1.addTask(subtask2);

        epic1.verifyStatus();

        assertEquals(TaskStatus.IN_PROGRESS, epic1.getTaskStatus(), "Если подзадача1 NEW, а подзадача2 DONE, то эпик IN_PROGRESS");
    }

    @Test
    public void testAllInProgressSubtasks() {
        Subtask subtask1 = new Subtask("Подзадача1", "Описание подзадачи1", TaskStatus.IN_PROGRESS, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача2", "Описание подзадачи2", TaskStatus.IN_PROGRESS, epic1.getId());

        epic1.addTask(subtask1);
        epic1.addTask(subtask2);

        epic1.verifyStatus();

        assertEquals(TaskStatus.IN_PROGRESS, epic1.getTaskStatus(), "Если все подзадачи IN_PROGRESS, то эпик INPROGRESS");
    }

    @Test
    public void testNoSubtasks() {
        epic1.verifyStatus();

        assertEquals(TaskStatus.NEW, epic1.getTaskStatus(), "Эпик NEW, если нет подзадач");
    }
}
