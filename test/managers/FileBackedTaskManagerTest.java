package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;

public class FileBackedTaskManagerTest {
    TaskManager taskManager;
    File tempFile;

    @BeforeEach
    public void beforeEach() {
        try {
            tempFile = File.createTempFile("kanban-", ".tmp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        taskManager = Managers.loadFromFile(tempFile.getPath());
        tempFile.deleteOnExit();
    }

    @Test
    public void loadFromFileTest() {
        taskManager.addTask(new Task("Задача1", "Задача1 Описание", TaskStatus.NEW));
        taskManager.addTask(new Task("Задача2", "Задача2 Описание", TaskStatus.IN_PROGRESS));
        taskManager.addTask(new Task("Задача3", "Задача3 Описание", TaskStatus.DONE));
        taskManager.addEpic(new Epic("Эпик1", "Эпик1 Описание"));
        taskManager.addSubtask(new Subtask("Подзадача1", "Подзадача1 Описание", TaskStatus.DONE, taskManager.getEpics().getFirst().getId()));

        TaskManager newTaskManager = Managers.loadFromFile(tempFile.getPath());
        Assertions.assertEquals(taskManager.getTasks(), newTaskManager.getTasks(), "Не совпадают задачи");
        Assertions.assertEquals(taskManager.getEpics(), newTaskManager.getEpics(), "Не совпадают эпики");
        Assertions.assertEquals(taskManager.getSubtasks(), newTaskManager.getSubtasks(), "Не совпадают подзадачи");
    }

    @Test
    public void loadFromFileEmptyTest() {
        TaskManager newTaskManager = Managers.loadFromFile(tempFile.getPath());
        Assertions.assertEquals(taskManager.getTasks(), newTaskManager.getTasks(), "Задачи должны быть пустыми");
        Assertions.assertEquals(taskManager.getEpics(), newTaskManager.getEpics(), "Эпики должны быть пустыми");
        Assertions.assertEquals(taskManager.getSubtasks(), newTaskManager.getSubtasks(), "Подзадачи должны быть пустыми");
    }

    @Test
    public void nonExistentFileGiven() {
        TaskManager newTaskManager = Managers.loadFromFile("non-existent-file");
        Assertions.assertEquals(taskManager.getTasks(), newTaskManager.getTasks(), "Задачи должны быть пустыми");
        Assertions.assertEquals(taskManager.getEpics(), newTaskManager.getEpics(), "Эпики должны быть пустыми");
        Assertions.assertEquals(taskManager.getSubtasks(), newTaskManager.getSubtasks(), "Подзадачи должны быть пустыми");

        File nonExistentFile = new File("non-existent-file");
        if (nonExistentFile.exists()) {
            nonExistentFile.delete();
        }
    }
}
