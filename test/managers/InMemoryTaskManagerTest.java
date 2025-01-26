package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void testIsOverlapping() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        // Создаём задачи с различными временными интервалами
        Task task1 = new Task("Задача1", "Задача1", TaskStatus.NEW);
        task1.setStartTime(LocalDateTime.of(2025, 1, 26, 10, 0));
        task1.setDuration(Duration.ofHours(1)); // 1 час

        Task task2 = new Task("Задача2", "Задача2", TaskStatus.NEW);
        task2.setStartTime(LocalDateTime.of(2025, 1, 26, 10, 30)); // Пересекается с task1
        task2.setDuration(Duration.ofHours(1));

        Task task3 = new Task("Задача3", "Задача3", TaskStatus.NEW);
        task3.setStartTime(LocalDateTime.of(2025, 1, 26, 12, 0)); // Не пересекается с task1 и task2
        task3.setDuration(Duration.ofHours(1));

        Task task4 = new Task("Задача4", "Задача4", TaskStatus.NEW);
        task4.setStartTime(null);
        task4.setDuration(Duration.ofHours(1));

        // Проверяем пересечение
        assertTrue(taskManager.isOverlapping(task1, task2), "Задачи должны пересекаться");
        assertFalse(taskManager.isOverlapping(task1, task3), "Задачи не должны пересекаться");
        assertFalse(taskManager.isOverlapping(task2, task3), "Задачи не должны пересекаться");
        assertFalse(taskManager.isOverlapping(task1, task4), "Задача с null временем не должна пересекаться");
        assertFalse(taskManager.isOverlapping(task4, task3), "Задача с null временем не должна пересекаться");

        // Задачи с одинаковыми временными интервалами
        Task task5 = new Task("Задача5", "Задача5", TaskStatus.NEW);
        task5.setStartTime(LocalDateTime.of(2025, 1, 26, 10, 0)); // Задача с таким же временем начала и конца
        task5.setDuration(Duration.ofHours(1));

        assertTrue(taskManager.isOverlapping(task1, task5), "Задачи с одинаковыми интервалами должны пересекаться");
    }
}
