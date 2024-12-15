package Managers;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class InMemoryTaskManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        taskManager.addTask(task);

        int taskId = task.getId();

        final Task savedTask = taskManager.getTask(taskId);

        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        Assertions.assertNotNull(tasks, "Задачи не возвращаются.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void doesDeleteTaskActuallyDeletesTaskFromTaskManager() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        taskManager.addTask(task);

        int taskId = task.getId();

        taskManager.deleteTask(taskId);

        final List<Task> tasks = taskManager.getTasks();

        Assertions.assertEquals(0, tasks.size(), "Задача не удалена.");
    }

    @Test
    public void epicShouldNotHaveNonExistingSubtasks() {
        Epic epic1 = new Epic("Эпик1", "Эпик1 описание");
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача1", "Подзадача1 описание", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача2", "Подзадача2 описание", TaskStatus.NEW, epic1.getId());

        epic1.addTask(subtask1);
        epic1.addTask(subtask2);

        // Удаление подзадач
        epic1.deleteTask(subtask1.getId());
        epic1.deleteTask(subtask2.getId());

        Assertions.assertEquals(0, epic1.getTasks().size(), "Подзадачи не удалены.");
        Assertions.assertEquals(0, taskManager.getSubtasks().size(), "Подзадачи не удалены.");
    }
}
