package managers;

import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void doesAddingNewTaskIncrementsHistorySize() {
        assertEquals(0, historyManager.getHistory().size(), "Изначальная история не пуста");

        Task task1 = new Task("Задача1", "Описание задачи", TaskStatus.NEW);
        historyManager.add(task1);

        assertEquals(1, historyManager.getHistory().size(), "Размер истории не увеличился при добавлении новой задачи");
    }

//    @Test
//    public void canHistoryReachMoreThanTenTasks() {
//        Task task1 = new Task("Задача1", "Описание задачи", TaskStatus.NEW);
//        for (int i = 0; i < 11; i++) {
//            historyManager.add(task1);
//        }
//
//        Assertions.assertEquals(10, historyManager.getHistory().size(), "Размер истории привысил 10 элементов");
//    }

//    @Test
//    public void doesEleventhTaskDeletesFirstOneInHistory() {
//        Task task1 = new Task("Задача1", "Описание задачи", TaskStatus.NEW);
//        Task task2 = new Task("Задача2", "Описание задачи", TaskStatus.NEW);
//        Task task3 = new Task("Задача3", "Описание задачи", TaskStatus.NEW);
//
//        historyManager.add(task1);
//
//        for (int i = 0; i < 9; i++) {
//            historyManager.add(task2);
//        }
//
//        historyManager.add(task3);
//
//        Assertions.assertNotEquals("Задача1", historyManager.getHistory().getFirst().getName(), "Задача1 не была удалена из истории");
//        Assertions.assertEquals("Задача3", historyManager.getHistory().getLast().getName(), "Задача3 не была найдена в истории");
//    }
    // Два вышеуказанных теста больше не нужны т.к. история теперь неограниченная

    @Test
    public void doesDeleteTaskWorks() {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Задача1", "Описание задачи", TaskStatus.NEW);
        Task task2 = new Task("Задача2", "Описание задачи", TaskStatus.NEW);
        Task task3 = new Task("Задача3", "Описание задачи", TaskStatus.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        // TaskManager нужен для того, чтобы задачи имели id

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        assertEquals(2, historyManager.getHistory().size(), "Размер истории не изменился");
        assertEquals("Задача1", historyManager.getHistory().getFirst().getName(), "Задача1 не была найдена в истории");
        assertEquals("Задача3", historyManager.getHistory().getLast().getName(), "Задача3 не была найдена в истории");
    }

    @Test
    public void accessingNewTaskOverridesItsLastUsage() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Задача1", "Описание задачи", TaskStatus.NEW);
        Task task2 = new Task("Задача2", "Описание задачи", TaskStatus.NEW);
        Task task3 = new Task("Задача3", "Описание задачи", TaskStatus.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.add(task1);

        assertEquals("Задача1", historyManager.getHistory().getLast().getName(), "Задача1 не является последней запрошенной");
        assertEquals("Задача2", historyManager.getHistory().getFirst().getName(), "Задача2 не является первой запрошенной");
        assertEquals("Задача3", historyManager.getHistory().get(1).getName(), "Задача3 не вторая запрошенная в истории");
    }

    @Test
    public void testEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История задач должна быть пустой");
    }

    @Test
    public void testDuplicateTasks() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.DONE);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать 2 задачи");

        historyManager.add(task1);

        history = historyManager.getHistory();
        assertEquals(2, history.size(), "История не должна содержать дублирующуюся задачу");
        assertEquals(task2, history.getFirst(), "Задача должна быть в начале истории");
    }

    @Test
    public void testRemoveTaskFromHistory() {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        Task task3 = new Task("Задача 3", "Описание 3", TaskStatus.IN_PROGRESS);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        // Добавляем задачи в историю
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // Проверяем, что все задачи в истории
        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "История должна содержать 3 задачи");

        // Удаляем задачу из начала
        historyManager.remove(1);
        history = historyManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать 2 задачи");
        assertFalse(history.contains(task1), "Задача 1 должна быть удалена");

        // Удаляем задачу из середины
        historyManager.remove(2);
        history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать 1 задачу");
        assertFalse(history.contains(task2), "Задача 2 должна быть удалена");

        // Удаляем задачу из конца
        historyManager.remove(3);
        history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой");
    }
}
