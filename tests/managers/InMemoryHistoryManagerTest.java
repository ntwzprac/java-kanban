package Managers;

import Tasks.Epic;
import Tasks.Task;
import Tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void doesAddingNewTaskIncrementsHistorySize() {
        Assertions.assertEquals(0, historyManager.getHistory().size(), "Изначальная история не пуста");

        Task task1 = new Task("Задача1", "Описание задачи", TaskStatus.NEW);
        historyManager.add(task1);

        Assertions.assertEquals(1, historyManager.getHistory().size(), "Размер истории не увеличился при добавлении новой задачи");
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

        Assertions.assertEquals(2, historyManager.getHistory().size(), "Размер истории не изменился");
        Assertions.assertEquals("Задача1", historyManager.getHistory().getFirst().getName(), "Задача1 не была найдена в истории");
        Assertions.assertEquals("Задача3", historyManager.getHistory().getLast().getName(), "Задача3 не была найдена в истории");
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

        Assertions.assertEquals("Задача1", historyManager.getHistory().getLast().getName(), "Задача1 не является последней запрошенной");
        Assertions.assertEquals("Задача2", historyManager.getHistory().getFirst().getName(), "Задача2 не является первой запрошенной");
        Assertions.assertEquals("Задача3", historyManager.getHistory().get(1).getName(), "Задача3 не вторая запрошенная в истории");
    }
}
